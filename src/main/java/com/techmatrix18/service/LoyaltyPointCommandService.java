package com.techmatrix18.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.model.LoyaltyPoint;
import com.techmatrix18.model.LoyaltyPointIdempotency;
import com.techmatrix18.model.OutboxEvent;
import com.techmatrix18.repository.LoyaltyPointIdempotencyRepository;
import com.techmatrix18.repository.LoyaltyPointRepository;
import com.techmatrix18.repository.OutboxEventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service class for handling commands related to loyalty points, including earning and redeeming points.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 04.06.2026
 */
@Service
public class LoyaltyPointCommandService {

    private final LoyaltyPointRepository loyaltyRepo;
    private final LoyaltyPointIdempotencyRepository idempotencyRepo;
    private final OutboxEventRepository outboxRepo; // Используем ОБЩУЮ таблицу Outbox
    private final ObjectMapper objectMapper;
    private final TransactionalOperator txOperator;

    // Внедряем все необходимые зависимости для Command-стороны
    public LoyaltyPointCommandService(LoyaltyPointRepository loyaltyRepo,
                                      LoyaltyPointIdempotencyRepository idempotencyRepo,
                                      OutboxEventRepository outboxRepo,
                                      ObjectMapper objectMapper,
                                      TransactionalOperator txOperator) {
        this.loyaltyRepo = loyaltyRepo;
        this.idempotencyRepo = idempotencyRepo;
        this.outboxRepo = outboxRepo;
        this.objectMapper = objectMapper;
        this.txOperator = txOperator;
    }

    /**
     * Начисление баллов лояльности (С Идемпотентностью и Outbox)
     */
    public Mono<LoyaltyPoint> earnPoints(String idempotencyKey, Long creditCardId, Long pointsToEarn, LocalDateTime expirationDate) {
        if (pointsToEarn <= 0) {
            return Mono.error(new IllegalArgumentException("Points to earn must be positive"));
        }

        return checkAndLock(idempotencyKey) // 1. Проверяем дубликат запроса
            .then(loyaltyRepo.findByCreditCardId(creditCardId))
            // Если для этой карты еще нет записи в таблице бонусов, создаем новую структуру
            .defaultIfEmpty(createNewLoyaltyRecord(creditCardId))
            .flatMap(loyaltyPoint -> {
                // Изменяем состояние баллов в памяти
                loyaltyPoint.setPointsBalance(loyaltyPoint.getPointsBalance() + pointsToEarn);
                loyaltyPoint.setLastUpdated(LocalDateTime.now());
                if (expirationDate != null) {
                    loyaltyPoint.setExpirationDate(expirationDate);
                }

                // 2. Сохраняем Write-модель в БД
                return loyaltyRepo.save(loyaltyPoint)
                    // 3. Записываем событие в ОБЩИЙ outbox
                    .flatMap(savedPoints -> saveOutboxEvent("PointsEarned", savedPoints)
                        // 4. Закрываем ключ идемпотентности как COMPLETED
                        .then(confirmIdempotency(idempotencyKey))
                        .thenReturn(savedPoints));
            })
            .as(txOperator::transactional); // Вся операция строго атомарна
    }

    /**
     * Списание баллов лояльности (С Идемпотентностью и Outbox)
     */
    public Mono<LoyaltyPoint> spendPoints(String idempotencyKey, Long creditCardId, Long pointsToSpend) {
        if (pointsToSpend <= 0) {
            return Mono.error(new IllegalArgumentException("Points to spend must be positive"));
        }

        return checkAndLock(idempotencyKey)
            .then(loyaltyRepo.findByCreditCardId(creditCardId))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Loyalty account not found for this card")))
            .flatMap(loyaltyPoint -> {
                // Проверяем, хватает ли бонусов для списания
                if (loyaltyPoint.getPointsBalance() < pointsToSpend) {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough loyalty points balance"));
                }

                loyaltyPoint.setPointsBalance(loyaltyPoint.getPointsBalance() - pointsToSpend);
                loyaltyPoint.setLastUpdated(LocalDateTime.now());

                return loyaltyRepo.save(loyaltyPoint)
                    .flatMap(savedPoints -> saveOutboxEvent("PointsSpent", savedPoints)
                        .then(confirmIdempotency(idempotencyKey))
                        .thenReturn(savedPoints));
            })
            .as(txOperator::transactional);
    }

    /**
     * Вспомогательный переиспользуемый метод создания записи в ОБЩЕМ Outbox
     */
    private Mono<LoyaltyPoint> saveOutboxEvent(String eventType, LoyaltyPoint loyaltyPoint) {
        try {
            String payloadJson = objectMapper.writeValueAsString(loyaltyPoint);

            OutboxEvent outboxEvent = new OutboxEvent();
            outboxEvent.setId(UUID.randomUUID());
            outboxEvent.setAggregateType("LoyaltyPoints"); // Указываем новый тип сущности!
            outboxEvent.setAggregateId(String.valueOf(loyaltyPoint.getCreditCardId()));
            outboxEvent.setEventType(eventType);
            outboxEvent.setPayload(payloadJson);
            outboxEvent.setCreatedAt(LocalDateTime.now());
            outboxEvent.setProcessed(false);

            return outboxRepo.save(outboxEvent).thenReturn(loyaltyPoint);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to serialize LoyaltyPoint payload for Outbox", e));
        }
    }

    /**
     * Проверка и блокировка ключа идемпотентности (для loyalty_points_idempotency)
     */
    private Mono<Boolean> checkAndLock(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return Mono.just(true);
        }
        return idempotencyRepo.existsById(idempotencyKey)
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate loyalty request detected"));
                }
                LoyaltyPointIdempotency record = new LoyaltyPointIdempotency();
                record.setIdempotencyKey(idempotencyKey);
                record.setStatus("PROCESSING");
                record.setCreatedAt(LocalDateTime.now());
                record.setNew(true); // Для R2DBC делать строго INSERT
                return idempotencyRepo.save(record).thenReturn(true);
            });
    }

    /**
     * Фиксация успешного выполнения операции по ключу идемпотентности
     */
    private Mono<Void> confirmIdempotency(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) return Mono.empty();
        return idempotencyRepo.findById(idempotencyKey)
            .flatMap(record -> {
                record.setStatus("COMPLETED");
                record.setNew(false); // Для R2DBC делать UPDATE
                return idempotencyRepo.save(record);
            }).then();
    }

    private LoyaltyPoint createNewLoyaltyRecord(Long creditCardId) {
        LoyaltyPoint lp = new LoyaltyPoint();
        lp.setCreditCardId(creditCardId);
        lp.setPointsBalance(0L);
        lp.setLastUpdated(LocalDateTime.now());
        return lp;
    }
}

