package com.techmatrix18.service;

import com.techmatrix18.model.CreditCardCqrsRead;
import com.techmatrix18.repository.CreditCardCqrsReadRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Service class for managing CQRS Read operations for Credit Cards.
 * Reads data exclusively from the denormalized, read-optimized table.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.2
 * @since 02.06.2026
 */
@Service
public class CreditCardQueryService {
    private final CreditCardCqrsReadRepository cqrsReadRepo;

    public CreditCardQueryService(CreditCardCqrsReadRepository cqrsReadRepo) {
        this.cqrsReadRepo = cqrsReadRepo;
    }

    public Flux<CreditCardCqrsRead> findAll() {
        return cqrsReadRepo.findAll();
    }

    public Mono<CreditCardCqrsRead> findById(Long id) {
        return cqrsReadRepo.findById(id);
    }

    public Flux<CreditCardCqrsRead> findByUserId(Long userId) {
        return cqrsReadRepo.findByUserId(userId);
    }

    public Flux<CreditCardCqrsRead> findByBalanceBetween(BigDecimal minBalance, BigDecimal maxBalance) {
        return cqrsReadRepo.findByBalanceBetween(minBalance, maxBalance);
    }

    public Flux<CreditCardCqrsRead> findByIsBlocked(Boolean isBlocked) {
        return cqrsReadRepo.findByIsBlocked(isBlocked);
    }

    public Flux<CreditCardCqrsRead> findByBalanceLessThan(BigDecimal balance) {
        return cqrsReadRepo.findByBalanceLessThan(balance);
    }

    public Flux<CreditCardCqrsRead> findByBalanceGreaterThanEqual(BigDecimal balance) {
        return cqrsReadRepo.findByBalanceGreaterThanEqual(balance);
    }

    public Mono<CreditCardCqrsRead> findByMaskedCardNumber(String maskedCardNumber) {
        return cqrsReadRepo.findByMaskedCardNumber(maskedCardNumber);
    }

    public Mono<CreditCardCqrsRead> findByMaskedCardNumberAndIsBlocked(String maskedCardNumber, Boolean isBlocked) {
        return cqrsReadRepo.findByMaskedCardNumberAndIsBlocked(maskedCardNumber, isBlocked);
    }
}

