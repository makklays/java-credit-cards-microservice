package com.techmatrix18.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.model.CreditCard;
import com.techmatrix18.model.CreditCardIdempotency;
import com.techmatrix18.model.OutboxEvent;
import com.techmatrix18.repository.CreditCardIdempotencyRepository;
import com.techmatrix18.repository.CreditCardRepository;
import com.techmatrix18.repository.OutboxEventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service class for managing CreditCard Commands with CQRS and Idempotency.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 03.06.2026
 */
@Service
public class CreditCardCommandService {

    private final CreditCardRepository creditCardRepo;
    private final CreditCardIdempotencyRepository idempotencyRepo;
    private final OutboxEventRepository outboxRepo;
    private final ObjectMapper objectMapper;
    private final TransactionalOperator txOperator;

    // Внедряем все репозитории, включая Outbox и ObjectMapper для сериализации
    public CreditCardCommandService(CreditCardRepository creditCardRepo,
                                    CreditCardIdempotencyRepository idempotencyRepo,
                                    OutboxEventRepository outboxRepo,
                                    ObjectMapper objectMapper,
                                    TransactionalOperator txOperator) {
        this.creditCardRepo = creditCardRepo;
        this.idempotencyRepo = idempotencyRepo;
        this.outboxRepo = outboxRepo;
        this.objectMapper = objectMapper;
        this.txOperator = txOperator;
    }

    /**
     * Создание новой карты с логикой Идемпотентности и Outbox
     */
    public Mono<CreditCard> createCard(String idempotencyKey, CreditCard card) {
        // 1. Проверяем и блокируем ключ идемпотентности на входе
        return checkAndLock(idempotencyKey)
            .then(Mono.defer(() -> {
                card.setCreatedAt(LocalDateTime.now());
                card.setUpdatedAt(LocalDateTime.now());

                if (card.getStatus() == null) {
                    card.setStatus(com.techmatrix18.enums.CreditCardStatus.EXPIRED);
                }
                if (card.getType() == null) {
                    card.setType(com.techmatrix18.enums.CreditCardType.VISA);
                }

                // 2. Сохраняем карту в основную таблицу
                return creditCardRepo.save(card)
                    // 3. Пишем событие в Outbox
                    .flatMap(savedCard -> saveOutboxEvent("CardCreated", savedCard)
                        // 4. Закрываем ключ идемпотентности как COMPLETED
                        .then(confirmIdempotency(idempotencyKey))
                        .thenReturn(savedCard));
            }))
            // Гарантируем, что INSERT карты, INSERT события в Outbox и UPDATE статуса идемпотентности
            // выполнятся строго в одной транзакции БД. Всё или ничего.
            .as(txOperator::transactional); // Вся операция строго атомарна
    }

    /**
     * Пакетное сохранение карт через поток (Flux) с логикой Outbox
     */
    public Flux<CreditCard> saveAll(Mono<CreditCard> creditCardMono) {
        return creditCardMono
            .flatMap(card -> {
                if (card.getCreatedAt() == null) {
                    card.setCreatedAt(LocalDateTime.now());
                }
                card.setUpdatedAt(LocalDateTime.now());

                return creditCardRepo.save(card)
                    .flatMap(savedCard -> saveOutboxEvent("CardCreated", savedCard));
            })
            .flux()
            .as(txOperator::transactional);
    }

    /**
     * Пополнение баланса карты (С Idempotency и Outbox-событием BalanceUpdated)
     */
    public Mono<CreditCard> addMoney(String idempotencyKey, Long creditCardId, BigDecimal amount) {
        return checkAndLock(idempotencyKey)
            .then(creditCardRepo.findById(creditCardId))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found")))
            .flatMap(card -> {
                if (amount.signum() <= 0) {
                    return Mono.error(new IllegalArgumentException("Amount must be positive"));
                }
                card.setBalance(card.getBalance().add(amount));
                card.setUpdatedAt(LocalDateTime.now());

                // Сохраняем карту, пишем в аутбокс "BalanceUpdated", закрываем ключ
                return creditCardRepo.save(card)
                    .flatMap(savedCard -> saveOutboxEvent("BalanceUpdated", savedCard)
                        .then(confirmIdempotency(idempotencyKey))
                        .thenReturn(savedCard));
            })
            .as(txOperator::transactional);
    }

    /**
     * Списание средств с карты (С Idempotency и Outbox-событием MoneyCharged)
     */
    public Mono<CreditCard> chargeMoney(String idempotencyKey, Long creditCardId, BigDecimal amount) {
        return checkAndLock(idempotencyKey)
            .then(creditCardRepo.findById(creditCardId))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found")))
            .flatMap(card -> {
                if (amount.signum() <= 0) {
                    return Mono.error(new IllegalArgumentException("Amount must be positive"));
                }

                BigDecimal available = card.getBalance().add(card.getCreditLimit());
                if (available.compareTo(amount) < 0) {
                    return Mono.error(new IllegalStateException("Credit limit exceeded"));
                }

                card.setBalance(card.getBalance().subtract(amount));
                card.setUpdatedAt(LocalDateTime.now());

                // Сохраняем карту, пишем в аутбокс "MoneyCharged", закрываем ключ
                return creditCardRepo.save(card)
                    .flatMap(savedCard -> saveOutboxEvent("MoneyCharged", savedCard)
                        .then(confirmIdempotency(idempotencyKey))
                        .thenReturn(savedCard));
            })
            .as(txOperator::transactional);
    }

    /**
     * Вспомогательный переиспользуемый метод создания записи в Outbox в рамках текущей транзакции
     */
    private Mono<CreditCard> saveOutboxEvent(String eventType, CreditCard card) {
        try {
            String payloadJson = objectMapper.writeValueAsString(card);

            OutboxEvent outboxEvent = new OutboxEvent();
            outboxEvent.setId(UUID.randomUUID());
            outboxEvent.setAggregateType("CreditCard");
            outboxEvent.setAggregateId(String.valueOf(card.getId()));
            outboxEvent.setEventType(eventType);
            outboxEvent.setPayload(payloadJson);
            outboxEvent.setCreatedAt(LocalDateTime.now());
            outboxEvent.setProcessed(false);

            return outboxRepo.save(outboxEvent).thenReturn(card);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to serialize CreditCard payload for Outbox", e));
        }
    }

    /**
     * Проверка и блокировка ключа идемпотентности
     */
    private Mono<Boolean> checkAndLock(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return Mono.just(true);
        }
        return idempotencyRepo.existsById(idempotencyKey)
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate request detected"));
                }
                CreditCardIdempotency record = new CreditCardIdempotency();
                record.setIdempotencyKey(idempotencyKey);
                record.setStatus("PROCESSING");
                record.setCreatedAt(LocalDateTime.now());
                record.setNew(true);
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
                record.setNew(false);
                return idempotencyRepo.save(record);
            }).then();
    }

    /**
     * Перевод денег между двумя кредитными картами (С Idempotency и Outbox)
     *
     * @param idempotencyKey Уникальный UUID токен запроса от клиента
     * @param fromCardId     ID карты, с которой списываем деньги
     * @param toCardId       ID карты, на которую зачисляем деньги
     * @param amount         Сумма перевода
     */
    public Mono<Void> transferMoney(String idempotencyKey, Long fromCardId, Long toCardId, BigDecimal amount) {
        // Базовые валидации аргументов
        if (fromCardId.equals(toCardId)) {
            return Mono.error(new IllegalArgumentException("Cannot transfer money to the same card"));
        }
        if (amount.signum() <= 0) {
            return Mono.error(new IllegalArgumentException("Amount must be positive"));
        }

        // 1. Проверяем и блокируем ключ идемпотентности на входе
        return checkAndLock(idempotencyKey)
            // 2. Ищем карту списания (Source Card)
            .then(creditCardRepo.findById(fromCardId))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Source card not found")))
            .flatMap(fromCard -> {
                // Проверяем, хватает ли денег с учетом кредитного лимита
                BigDecimal availableFunds = fromCard.getBalance().add(fromCard.getCreditLimit());
                if (availableFunds.compareTo(amount) < 0) {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit limit exceeded on source card"));
                }

                // 3. Ищем карту зачисления (Destination Card)
                return creditCardRepo.findById(toCardId)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination card not found")))
                    .flatMap(toCard -> {
                        // Изменяем балансы и таймстампы для обеих карт в памяти
                        fromCard.setBalance(fromCard.getBalance().subtract(amount));
                        fromCard.setUpdatedAt(LocalDateTime.now());

                        toCard.setBalance(toCard.getBalance().add(amount));
                        toCard.setUpdatedAt(LocalDateTime.now());

                        // 4. Сохраняем всё в БД в рамках ОДНОЙ неделимой транзакции:
                        // Сначала обновляем первую карту -> пишем её событие в Outbox
                        return creditCardRepo.save(fromCard)
                            .flatMap(savedFrom -> saveOutboxEvent("MoneyCharged", savedFrom))
                            // Затем обновляем вторую карту -> пишем её событие в Outbox
                            .then(creditCardRepo.save(toCard))
                            .flatMap(savedTo -> saveOutboxEvent("BalanceUpdated", savedTo))
                            // Закрываем ключ идемпотентности (меняем статус на COMPLETED)
                            .then(confirmIdempotency(idempotencyKey));
                    });
            })
            // txOperator гарантирует: если упадет хоть один save или упадет сеть,
            // вся цепочка откатится, и балансы карт не рассинхронизируются.
            .as(txOperator::transactional)
            .then(); // Возвращаем Mono<Void> в знак успешного окончания
    }
}

