package com.techmatrix18.controller;

import com.techmatrix18.model.CreditCard;
import com.techmatrix18.model.CreditCardCqrsRead;
import com.techmatrix18.service.CreditCardCommandService;
import com.techmatrix18.service.CreditCardQueryService;
import com.techmatrix18.service.TelegramService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

/**
 * REST controller for CreditCard endpoints with CQRS and Idempotency.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @since 03.06.2026
 * @version 0.0.1
 */
@RestController
@RequestMapping(path = "/api/v1/credit-cards", produces = "application/json")
@CrossOrigin(origins = "*")
public class CreditCardController {

    private final CreditCardCommandService creditCardCommandService;
    private final CreditCardQueryService creditCardQueryService;
    private final TelegramService telegramService;

    public CreditCardController(CreditCardCommandService creditCardCommandService,
                                CreditCardQueryService creditCardQueryService,
                                TelegramService telegramService) {
        this.creditCardCommandService = creditCardCommandService;
        this.creditCardQueryService = creditCardQueryService;
        this.telegramService = telegramService;
    }

    @GetMapping("/hello")
    public Mono<String> hello() {
        // send telegram message - test only
        telegramService.sendMessage(123456789L, "Hello from CreditCardController!");
        // return greeting message - test only
        return Mono.just("Hello, reactive world!");
    }

    // =========================================================================
    //   CQRS: ВЕТКА ЧТЕНИЯ (QUERIES) -> Возвращают CreditCardCqrsRead
    // =========================================================================
    @GetMapping(params = "recent")
    public Flux<CreditCardCqrsRead> recentCreditCard() {
        return creditCardQueryService.findAll().take(12);
    }

    /**
     * Get credit card by ID (Using CQRS read-optimized table)
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Mono<CreditCardCqrsRead> creditCardById(@PathVariable("id") Long id) {
        return creditCardQueryService.findById(id);
    }

    // =========================================================================
    //   CQRS + ИДЕМПОТЕНТНОСТЬ: ВЕТКА ЗАПИСИ (COMMANDS) -> Меняют состояние
    // =========================================================================
    /**
     * Operation to create a new credit card (Now protected by Idempotency)
     */
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreditCard> postCreditCard(
            @RequestHeader("X-Idempotency-Key") String idempotencyKey,
            @RequestBody CreditCard creditCard) {

        // Передаем ключ идемпотентности, чтобы защитить систему от создания дубликатов карт
        return creditCardCommandService.createCard(idempotencyKey, creditCard);
    }

    /**
     * Operation to add money to credit card (Idempotent)
     *
     * @param idempotencyKey Secure token from client
     * @param creditCardId
     * @param amount
     * @return
     */
    @PostMapping("/deposit-money")
    @ResponseStatus(HttpStatus.OK) // Для успешной обработки существующей операции OK подходит лучше всего
    public Mono<CreditCard> postAddMoney(
            @RequestHeader("X-Idempotency-Key") String idempotencyKey,
            @RequestParam("credit-card") Long creditCardId,
            @RequestParam("amount") BigDecimal amount) {

        if (amount == null || amount.signum() <= 0) {
            return Mono.error(new IllegalArgumentException("Amount must be positive"));
        }
        return creditCardCommandService.addMoney(idempotencyKey, creditCardId, amount);
    }

    /**
     * Operation to charge money to credit card (Idempotent)
     *
     * @param idempotencyKey Secure token from client
     * @param creditCardId
     * @param amount
     * @return
     */
    @PostMapping("/withdrawal-money")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CreditCard> postChargeMoney(
            @RequestHeader("X-Idempotency-Key") String idempotencyKey,
            @RequestParam("credit-card") Long creditCardId,
            @RequestParam("amount") BigDecimal amount) {

        if (amount == null || amount.signum() <= 0) {
            return Mono.error(new IllegalArgumentException("Amount must be positive"));
        }
        return creditCardCommandService.chargeMoney(idempotencyKey, creditCardId, amount);
    }

    /**
     * Operation to transfer money between credit cards (Idempotent)
     *
     * @param idempotencyKey Secure token from client
     * @param fromCreditCardId
     * @param toCreditCardId
     * @param amount
     * @return
     */
    @PostMapping("/transfer-money")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Изменено на 244 No Content, так как команда ничего не возвращает
    public Mono<Void> postTransferMoney(
            @RequestHeader("X-Idempotency-Key") String idempotencyKey,
            @RequestParam("from-credit-card") Long fromCreditCardId,
            @RequestParam("to-credit-card") Long toCreditCardId,
            @RequestParam("amount") BigDecimal amount
    ) {
        if (amount == null || amount.signum() <= 0) {
            return Mono.error(new IllegalArgumentException("Amount must be positive"));
        }
        return creditCardCommandService.transferMoney(idempotencyKey, fromCreditCardId, toCreditCardId, amount);
    }
}

