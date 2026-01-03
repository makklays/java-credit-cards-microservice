package com.techmatrix18.controller;

import com.techmatrix18.model.CreditCard;
import com.techmatrix18.service.CreditCardService;
import com.techmatrix18.service.TelegramService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

/**
 * REST controller for CreditCard endpoints.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @since 25-12-2025
 * @version 0.0.1
 */
@RestController
@RequestMapping(path = "/api/v1/credit-cards", produces = "application/json")
@CrossOrigin(origins = "*")
public class CreditCardController {

    private final CreditCardService creditCardService;
    private final TelegramService telegramService;

    public CreditCardController(CreditCardService creditCardService, TelegramService telegramService) {
        this.creditCardService = creditCardService;
        this.telegramService = telegramService;
    }

    @GetMapping("/hello")
    public Mono<String> hello() {
        // send telegram message - test only
        telegramService.sendMessage(123456789L, "Hello from CreditCardController!");
        // return greeting message - test only
        return Mono.just("Hello, reactive world!");
    }

    @GetMapping(params = "recent")
    public Flux<CreditCard> recentCreditCard() {
        return creditCardService.findAll().take(12);
    }

    /**
     * Get credit card by ID
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Mono<CreditCard> creaditCardById(@PathVariable("id") Long id) {
        return creditCardService.findById(id);
    }

    /**
     * Create a new credit card
     *
     * @param creditCardMono
     * @return
     */
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreditCard> postCreditCard(@RequestBody Mono<CreditCard> creditCardMono) {
        return creditCardService.saveAll(creditCardMono).next();
    }

    /**
     * Operation to add money to credit card
     *
     * @param creditCardId
     * @param amount
     * @return
     */
    @PostMapping("/deposit-money")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreditCard> postAddMoney(@RequestParam("credit-card") Long creditCardId, @RequestParam("amount") BigDecimal amount) {
        if (amount.signum() <= 0) {
            return Mono.error(new IllegalArgumentException("Amount must be positive"));
        }
        return creditCardService.addMoney(creditCardId, amount);
    }

    /**
     * Operation to charge money to credit card
     *
     * @param creditCardId
     * @param amount
     * @return
     */
    @PostMapping("/withdrawal-money")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreditCard> postChargeMoney(@RequestParam("credit-card") Long creditCardId, @RequestParam("amount") BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return Mono.error(new IllegalArgumentException("Amount must be positive"));
        }
        return creditCardService.chargeMoney(creditCardId, amount);
    }

    /**
     * Operation to transfer money between credit cards
     *
     * @param fromCreditCardId
     * @param toCreditCardId
     * @param amount
     * @return
     */
    @PostMapping("/transfer-money")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreditCard> postTransferMoney(
            @RequestParam("from-credit-card") Long fromCreditCardId,
            @RequestParam("to-credit-card") Long toCreditCardId,
            @RequestParam("amount") BigDecimal amount
    ) {
        if (amount == null || amount.signum() <= 0) {
            return Mono.error(new IllegalArgumentException("Amount must be positive"));
        }
        return creditCardService.transferMoney(fromCreditCardId, toCreditCardId, amount);
    }
}

