package com.techmatrix18.service;

import com.techmatrix18.model.CreditCardCqrsRead;
import com.techmatrix18.repository.CreditCardCqrsReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

/**
 * Unit tests for the CreditCardQueryService class.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.06.2026
 */
@ExtendWith(MockitoExtension.class)
class CreditCardQueryServiceTest {

    @Mock
    private CreditCardCqrsReadRepository cqrsReadRepo;

    @InjectMocks
    private CreditCardQueryService queryService;

    private CreditCardCqrsRead card1;
    private CreditCardCqrsRead card2;

    @BeforeEach
    void setUp() {
        // Создаем тестовые данные с помощью вашего Builder
        card1 = CreditCardCqrsRead.builder()
            .cardId(1L)
            .userId(100L)
            .maskedCardNumber("4111****1111")
            .balance(BigDecimal.valueOf(500.00))
            .isBlocked(false)
            .build();

        card2 = CreditCardCqrsRead.builder()
            .cardId(2L)
            .userId(200L)
            .maskedCardNumber("5555****2222")
            .balance(BigDecimal.valueOf(1500.00))
            .isBlocked(true)
            .build();
    }

    @Test
    void shouldFindAll() {
        when(cqrsReadRepo.findAll()).thenReturn(Flux.just(card1, card2));

        Flux<CreditCardCqrsRead> result = queryService.findAll();

        StepVerifier.create(result)
            .expectNext(card1)
            .expectNext(card2)
            .verifyComplete();
    }

    @Test
    void shouldFindById() {
        when(cqrsReadRepo.findById(1L)).thenReturn(Mono.just(card1));

        Mono<CreditCardCqrsRead> result = queryService.findById(1L);

        StepVerifier.create(result)
            .expectNext(card1)
            .verifyComplete();
    }

    @Test
    void shouldFindByUserId() {
        when(cqrsReadRepo.findByUserId(100L)).thenReturn(Flux.just(card1));

        Flux<CreditCardCqrsRead> result = queryService.findByUserId(100L);

        StepVerifier.create(result)
            .expectNext(card1)
            .verifyComplete();
    }

    @Test
    void shouldFindByBalanceBetween() {
        BigDecimal min = BigDecimal.valueOf(100);
        BigDecimal max = BigDecimal.valueOf(1000);
        when(cqrsReadRepo.findByBalanceBetween(min, max)).thenReturn(Flux.just(card1));

        Flux<CreditCardCqrsRead> result = queryService.findByBalanceBetween(min, max);

        StepVerifier.create(result)
            .expectNext(card1)
            .verifyComplete();
    }

    @Test
    void shouldFindByIsBlocked() {
        when(cqrsReadRepo.findByIsBlocked(true)).thenReturn(Flux.just(card2));

        Flux<CreditCardCqrsRead> result = queryService.findByIsBlocked(true);

        StepVerifier.create(result)
            .expectNext(card2)
            .verifyComplete();
    }

    @Test
    void shouldFindByBalanceLessThan() {
        BigDecimal limit = BigDecimal.valueOf(1000.00);
        when(cqrsReadRepo.findByBalanceLessThan(limit)).thenReturn(Flux.just(card1));

        Flux<CreditCardCqrsRead> result = queryService.findByBalanceLessThan(limit);

        StepVerifier.create(result)
            .expectNext(card1)
            .verifyComplete();
    }

    @Test
    void shouldFindByBalanceGreaterThanEqual() {
        BigDecimal limit = BigDecimal.valueOf(1000.00);
        when(cqrsReadRepo.findByBalanceGreaterThanEqual(limit)).thenReturn(Flux.just(card2));

        Flux<CreditCardCqrsRead> result = queryService.findByBalanceGreaterThanEqual(limit);

        StepVerifier.create(result)
            .expectNext(card2)
            .verifyComplete();
    }

    @Test
    void shouldFindByMaskedCardNumber() {
        String mask = "4111****1111";
        when(cqrsReadRepo.findByMaskedCardNumber(mask)).thenReturn(Mono.just(card1));

        Mono<CreditCardCqrsRead> result = queryService.findByMaskedCardNumber(mask);

        StepVerifier.create(result)
            .expectNext(card1)
            .verifyComplete();
    }

    @Test
    void shouldFindByMaskedCardNumberAndIsBlocked() {
        String mask = "5555****2222";
        when(cqrsReadRepo.findByMaskedCardNumberAndIsBlocked(mask, true)).thenReturn(Mono.just(card2));

        Mono<CreditCardCqrsRead> result = queryService.findByMaskedCardNumberAndIsBlocked(mask, true);

        StepVerifier.create(result)
            .expectNext(card2)
            .verifyComplete();
    }
}

