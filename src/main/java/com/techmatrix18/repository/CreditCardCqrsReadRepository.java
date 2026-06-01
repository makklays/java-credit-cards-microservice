package com.techmatrix18.repository;

import com.techmatrix18.model.CreditCardCqrsRead;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Repository interface for CQRS Read operations on Credit Cards.
 * Handles fast lookups from the denormalized read-optimized table.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.06.2026
 */
@Repository
public interface CreditCardCqrsReadRepository extends ReactiveCrudRepository<CreditCardCqrsRead, Long> {

    // Быстрый поиск всех карт конкретного пользователя для главного экрана
    Flux<CreditCardCqrsRead> findByUserId(Long userId);

    @Query("SELECT * FROM credit_cards_cqrs_read WHERE balance > :minBalance")
    Flux<CreditCardCqrsRead> findRichAccounts(BigDecimal minBalance);

    Flux<CreditCardCqrsRead> findByBalanceBetween(BigDecimal minBalance, BigDecimal maxBalance);

    Flux<CreditCardCqrsRead> findByIsBlocked(Boolean isBlocked);

    Flux<CreditCardCqrsRead> findByBalanceLessThan(BigDecimal balance);

    Flux<CreditCardCqrsRead> findByBalanceGreaterThanEqual(BigDecimal balance);

    // В Read-модели поле называется maskedCardNumber (например, "**** 1234")
    Mono<CreditCardCqrsRead> findByMaskedCardNumber(String maskedCardNumber);

    Mono<CreditCardCqrsRead> findByMaskedCardNumberAndIsBlocked(String maskedCardNumber, Boolean isBlocked);
}

