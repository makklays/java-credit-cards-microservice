package com.techmatrix18.service;

import com.techmatrix18.model.CreditCard;
import com.techmatrix18.model.CreditCardCqrsRead;
import com.techmatrix18.model.CreditCardIdempotency;
import com.techmatrix18.repository.CreditCardCqrsReadRepository;
import com.techmatrix18.repository.CreditCardIdempotencyRepository;
import com.techmatrix18.repository.CreditCardRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service class for managing CreditCard Commands with CQRS and Idempotency.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.2
 * @since 02.06.2026
 */
@Service
public class CreditCardCommandService {

    private final CreditCardRepository creditCardRepo;
    private final CreditCardIdempotencyRepository idempotencyRepo;
    private final CreditCardCqrsReadRepository cqrsReadRepo;
    private final TransactionalOperator txOperator;

    public CreditCardCommandService(CreditCardRepository creditCardRepo,
                                    CreditCardIdempotencyRepository idempotencyRepo,
                                    CreditCardCqrsReadRepository cqrsReadRepo,
                                    TransactionalOperator txOperator) {
        this.creditCardRepo = creditCardRepo;
        this.idempotencyRepo = idempotencyRepo;
        this.cqrsReadRepo = cqrsReadRepo;
        this.txOperator = txOperator;
    }

    /**
     * Сохраняет поток кредитных карт и синхронизирует их с CQRS Read-витриной.
     */
    public Flux<CreditCard> saveAll(Mono<CreditCard> creditCardMono) {
        return creditCardMono
            .flatMap(card -> {
                // Если у карты нет таймстампов, проставим их перед сохранением
                if (card.getCreatedAt() == null) {
                    card.setCreatedAt(LocalDateTime.now());
                }
                card.setUpdatedAt(LocalDateTime.now());

                // 1. Сохраняем в основную таблицу (Write Model)
                return creditCardRepo.save(card)
                    // 2. Сразу же обновляем CQRS витрину (Read Model)
                    .flatMap(savedCard -> updateCqrsReadView(savedCard)
                        .thenReturn(savedCard)); // Возвращаем сохраненную Write-карту
            })
            // Превращаем в Flux, так как ваш контроллер ожидает Flux на выходе метода сервиса
            .flux()
            .as(txOperator::transactional); // Обертываем операцию в транзакцию
    }

    /**
     * Вспомогательный метод для обновления CQRS Read-витрины после изменения Write-модели.
     */
    public Mono<CreditCard> createCard(CreditCard card) {
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());

        // По умолчанию выставляем значения, если они не пришли в запросе
        if (card.getStatus() == null) {
            card.setStatus(com.techmatrix18.enums.CreditCardStatus.EXPIRED);
        }
        if (card.getType() == null) {
            card.setType(com.techmatrix18.enums.CreditCardType.VISA);
        }

        return creditCardRepo.save(card)
            .flatMap(savedCard -> updateCqrsReadView(savedCard).thenReturn(savedCard))
            .as(txOperator::transactional);
    }

    /**
     * Вспомогательный метод для проверки и сохранения ключа идемпотентности.
     */
    private Mono<Boolean> checkAndLock(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return Mono.just(true); // Если ключ не передан, пропускаем (не рекомендуется для финансов)
        }
        return idempotencyRepo.existsById(idempotencyKey)
            .flatMap(exists -> {
                if (exists) {
                    // Ключ уже обрабатывался — выкидываем ошибку (Conflict)
                    return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate request detected"));
                }
                // Ключа нет — создаем запись со статусом PROCESSING
                CreditCardIdempotency record = new CreditCardIdempotency();
                record.setIdempotencyKey(idempotencyKey);
                record.setStatus("PROCESSING");
                record.setCreatedAt(LocalDateTime.now());
                record.setNew(true); // Указываем R2DBC делать строго INSERT
                return idempotencyRepo.save(record).thenReturn(true);
            });
    }

    /**
     * Вспомогательный метод для фиксации успеха операции идемпотентности.
     */
    private Mono<Void> confirmIdempotency(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) return Mono.empty();
        return idempotencyRepo.findById(idempotencyKey)
            .flatMap(record -> {
                record.setStatus("COMPLETED");
                record.setNew(false); // Делаем UPDATE существующего ключа
                return idempotencyRepo.save(record);
            }).then();
    }

    /**
     * Вспомогательный метод для обновления CQRS Read-витрины (Синхронная проекция).
     */
    private Mono<CreditCardCqrsRead> updateCqrsReadView(CreditCard writeModel) {
        return cqrsReadRepo.findById(writeModel.getId())
            .defaultIfEmpty(new CreditCardCqrsRead()) // Если карты в Read-БД еще нет (например, при создании новой)
            .flatMap(readModel -> {
                if (readModel.getCardId() == null) {
                    readModel.setCardId(writeModel.getId());
                    readModel.setUserId(writeModel.getUserId());
                    readModel.setMaskedCardNumber(maskCardNumber(writeModel.getCardNumber()));
                    readModel.setNew(true); // INSERT
                } else {
                    readModel.setNew(false); // UPDATE
                }
                readModel.setBalance(writeModel.getBalance());
                readModel.setCreditLimit(writeModel.getCreditLimit());
                readModel.setCurrencyCode(writeModel.getCurrencyCode());
                readModel.setStatus(writeModel.getStatus());
                readModel.setType(writeModel.getType());
                readModel.setBankName(writeModel.getBankName());
                readModel.setIsBlocked(writeModel.getIsBlocked());
                readModel.setUpdatedAt(LocalDateTime.now());

                return cqrsReadRepo.save(readModel);
            });
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "****";
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    /**
     * Method to add money to a credit card (With Idempotency & CQRS update)
     */
    public Mono<CreditCard> addMoney(String idempotencyKey, Long creditCardId, BigDecimal amount) {
        return checkAndLock(idempotencyKey) // 1. Проверяем дубликат
            .then(creditCardRepo.findById(creditCardId))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found")))
            .flatMap(card -> {
                if (amount.signum() <= 0) {
                    return Mono.error(new IllegalArgumentException("Amount must be positive"));
                }
                card.setBalance(card.getBalance().add(amount));

                return creditCardRepo.save(card) // 2. Сохраняем Write-модель
                    .flatMap(savedCard -> updateCqrsReadView(savedCard) // 3. Обновляем Read-витрину
                        .then(confirmIdempotency(idempotencyKey)) // 4. Закрываем ключ идемпотентности
                        .thenReturn(savedCard));
            })
            .as(txOperator::transactional); // Вся цепочка атомарна
    }

    /**
     * Method to charge money from a credit card (With Idempotency & CQRS update)
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

                return creditCardRepo.save(card)
                    .flatMap(savedCard -> updateCqrsReadView(savedCard)
                        .then(confirmIdempotency(idempotencyKey))
                        .thenReturn(savedCard));
            })
            .as(txOperator::transactional);
    }

    /**
     * Method to transfer money between two credit cards (With Idempotency & CQRS update)
     */
    public Mono<Void> transferMoney(String idempotencyKey, Long fromId, Long toId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return Mono.error(new IllegalArgumentException("Amount must be positive"));
        }

        return checkAndLock(idempotencyKey)
            .then(Mono.zip(
                creditCardRepo.findById(fromId).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Source card not found"))),
                creditCardRepo.findById(toId).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Target card not found")))
            ))
            .flatMap(tuple -> {
                CreditCard from = tuple.getT1();
                CreditCard to = tuple.getT2();

                if (from.getBalance().compareTo(amount) < 0) {
                    return Mono.error(new IllegalStateException("Insufficient funds"));
                }

                from.setBalance(from.getBalance().subtract(amount));
                to.setBalance(to.getBalance().add(amount));

                // Сохраняем обе карты, обновляем обе витрины в CQRS, закрываем ключ
                return creditCardRepo.save(from)
                    .flatMap(this::updateCqrsReadView)
                    .then(creditCardRepo.save(to))
                    .flatMap(this::updateCqrsReadView)
                    .then(confirmIdempotency(idempotencyKey));
            })
            .as(txOperator::transactional)
            .then();
    }
}

