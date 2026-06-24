package com.techmatrix18.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.enums.CreditCardStatus;
import com.techmatrix18.enums.CreditCardType;
import com.techmatrix18.model.CreditCard;
import com.techmatrix18.model.CreditCardIdempotency;
import com.techmatrix18.model.OutboxEvent;
import com.techmatrix18.repository.CreditCardIdempotencyRepository;
import com.techmatrix18.repository.CreditCardRepository;
import com.techmatrix18.repository.OutboxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the CreditCardCommandService class.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.06.2026
 */
@ExtendWith(MockitoExtension.class)
public class CreditCardCommandServiceTest {
    @Mock private CreditCardRepository creditCardRepo;
    @Mock private CreditCardIdempotencyRepository idempotencyRepo;
    @Mock private OutboxEventRepository outboxRepo;
    @Mock private ObjectMapper objectMapper;
    @Mock private TransactionalOperator txOperator;

    @InjectMocks
    private CreditCardCommandService commandService;

    private CreditCard sampleCard;
    private final String key = "unique-uuid-123";

    @BeforeEach
    void setUp() {
        // Настройка пропускного режима для транзакций
        when(txOperator.transactional(any(Mono.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Создаем тестовую карту через ваш Builder
        sampleCard = CreditCard.builder()
            .id(1L)
            .userId(99L)
            .cardNumber("1111222233334444")
            .balance(BigDecimal.ZERO)
            .creditLimit(BigDecimal.valueOf(1000))
            .build();
    }

    @Test
    void createCard_Success() throws JsonProcessingException {
        // Given
        when(idempotencyRepo.existsById(key)).thenReturn(Mono.just(false));
        when(idempotencyRepo.save(any(CreditCardIdempotency.class))).thenReturn(Mono.just(new CreditCardIdempotency()));
        when(creditCardRepo.save(any(CreditCard.class))).thenReturn(Mono.just(sampleCard));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");
        when(outboxRepo.save(any(OutboxEvent.class))).thenReturn(Mono.just(new OutboxEvent()));
        when(idempotencyRepo.findById(key)).thenReturn(Mono.just(new CreditCardIdempotency()));

        // When
        Mono<CreditCard> result = commandService.createCard(key, sampleCard);

        // Then
        StepVerifier.create(result)
            .expectNextMatches(card -> {
                assertEquals(CreditCardStatus.EXPIRED, card.getStatus());
                assertEquals(CreditCardType.VISA, card.getType());
                return card.getId().equals(1L);
            })
            .verifyComplete();

        verify(creditCardRepo, times(1)).save(any(CreditCard.class));
        verify(outboxRepo, times(1)).save(any(OutboxEvent.class));
    }

    @Test
    void createCard_DuplicateKey_ThrowsConflict() {
        // Given: Ключ идемпотентности уже существует
        when(idempotencyRepo.existsById(key)).thenReturn(Mono.just(true));

        // When
        Mono<CreditCard> result = commandService.createCard(key, sampleCard);

        // Then
        StepVerifier.create(result)
            .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException
                    && ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.CONFLICT)
            .verify();

        // Проверяем, что до сохранения карты дело не дошло
        verify(creditCardRepo, never()).save(any());
    }

    @Test
    void addMoney_NegativeAmount_ThrowsIllegalArgumentException() {
        // Given
        when(idempotencyRepo.existsById(key)).thenReturn(Mono.just(false));
        when(idempotencyRepo.save(any())).thenReturn(Mono.just(new CreditCardIdempotency()));
        when(creditCardRepo.findById(1L)).thenReturn(Mono.just(sampleCard));

        // When: Передаем отрицательную сумму (-50)
        Mono<CreditCard> result = commandService.addMoney(key, 1L, BigDecimal.valueOf(-50));

        // Then
        StepVerifier.create(result)
            .expectError(IllegalArgumentException.class)
            .verify();

        verify(creditCardRepo, never()).save(any());
    }
}

