package com.techmatrix18.service;

import com.techmatrix18.model.CreditCard;
import com.techmatrix18.repository.CreditCardRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

/**
 * Service class for managing CreditCard entities.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.12.2025
 */
@Service
public class CreditCardService {
    private final CreditCardRepository creditCardRepo;
    private final TransactionalOperator txOperator;

    public CreditCardService (CreditCardRepository creditCardRepo, TransactionalOperator txOperator) {
        this.creditCardRepo = creditCardRepo;
        this.txOperator = txOperator;
    }

    public Flux<CreditCard> findAll() {
        return creditCardRepo.findAll();
    }

    public Mono<CreditCard> findById(Long id) {
        return creditCardRepo.findById(id);
    }

    public Flux<CreditCard> saveAll(Mono<CreditCard> creditCardMono) {
        return creditCardRepo.saveAll(creditCardMono);
    }

    public Flux<CreditCard> findByBalanceBetween(BigDecimal minBalance, BigDecimal maxBalance) {
        return creditCardRepo.findByBalanceBetween(minBalance, maxBalance);
    }

    public Flux<CreditCard> findByIsBlocked(Boolean isBlocked) {
        return creditCardRepo.findByIsBlocked(isBlocked);
    }

    public Flux<CreditCard> findByCardholderNameAndIsBlocked(String cardholderName, Boolean isBlocked) {
        return creditCardRepo.findByCardholderNameAndIsBlocked(cardholderName, isBlocked);
    }

    public Flux<CreditCard> findByBalanceLessThan(BigDecimal balance) {
        return creditCardRepo.findByBalanceLessThan(balance);
    }

    public Flux<CreditCard> findByBalanceGreaterThanEqual(BigDecimal balance) {
        return creditCardRepo.findByBalanceGreaterThanEqual(balance);
    }

    public Mono<CreditCard> findByCardNumber(String cardNumber) {
        return creditCardRepo.findByCardNumber(cardNumber);
    }

    public Mono<CreditCard> findByCardNumberAndCardholderName(String cardNumber, String cardholderName) {
        return creditCardRepo.findByCardNumberAndCardholderName(cardNumber, cardholderName);
    }

    public Mono<CreditCard> findByCardNumberAndIsBlocked(String cardNumber, Boolean isBlocked) {
        return creditCardRepo.findByCardNumberAndIsBlocked(cardNumber, isBlocked);
    }

    public Mono<CreditCard> findByCardholderNameAndCardNumberAndIsBlocked(String cardholderName, String cardNumber, Boolean isBlocked) {
        return creditCardRepo.findByCardholderNameAndCardNumberAndIsBlocked(cardholderName, cardNumber, isBlocked);
    }

    /**
     * Method to add money to a credit card
     *
     * @param creditCardId
     * @param amount
     * @return
     */
    public Mono<CreditCard> addMoney(Long creditCardId, BigDecimal amount) {
        return creditCardRepo.findById(creditCardId)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found")))
            .flatMap(card -> {
                if (amount.signum() <= 0) {
                    return Mono.error(new IllegalArgumentException("Amount must be positive"));
                }

                card.setBalance(card.getBalance().add(amount));
                return creditCardRepo.save(card);
            })
            .as(txOperator::transactional);
    }

    /**
     * Method to charge money from a credit card
     *
     * @param creditCardId
     * @param amount
     * @return
     */
    public Mono<CreditCard> chargeMoney(Long creditCardId, BigDecimal amount) {
        return creditCardRepo.findById(creditCardId)
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
                return creditCardRepo.save(card);
            })
            .as(txOperator::transactional);
    }

    /**
     * Method to transfer money between two credit cards
     *
     * @param fromId
     * @param toId
     * @param amount
     * @return
     */
    public Mono<CreditCard> transferMoney(Long fromId, Long toId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return Mono.error(new IllegalArgumentException("Amount must be positive"));
        }

        Mono<CreditCard> fromMono = creditCardRepo.findById(fromId)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Source card not found")));

        Mono<CreditCard> toMono = creditCardRepo.findById(toId)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Target card not found")));

        return Mono.zip(fromMono, toMono)
            .flatMap(tuple -> {
                CreditCard from = tuple.getT1();
                CreditCard to = tuple.getT2();

                if (from.getBalance().compareTo(amount) < 0) {
                    return Mono.error(new IllegalStateException("Insufficient funds"));
                }

                from.setBalance(from.getBalance().subtract(amount));
                to.setBalance(to.getBalance().add(amount));

                return creditCardRepo.save(from).then(creditCardRepo.save(to));
            })
            .as(txOperator::transactional);
    }
}

