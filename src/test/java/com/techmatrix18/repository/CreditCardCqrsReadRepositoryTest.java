package com.techmatrix18.repository;

import com.techmatrix18.model.CreditCardCqrsRead;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

/**
 * Integration tests for the CreditCardCqrsReadRepository.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.06.2026
 */
@DataR2dbcTest
@Testcontainers
public class CreditCardCqrsReadRepositoryTest {

    // 1. Объявляем и запускаем Docker-контейнер с PostgreSQL
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    // 2. Динамически подставляем порты и URL запущенного контейнера в настройки R2DBC
    @DynamicPropertySource
    static void r2dbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> String.format("r2dbc:postgresql://%s:%d/%s",
            postgres.getHost(),
            postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
            postgres.getDatabaseName()));
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);
    }

    @Autowired
    private CreditCardCqrsReadRepository repository;

    @Test
    void shouldFindByMaskedCardNumber() {
        CreditCardCqrsRead card = CreditCardCqrsRead.builder()
            .cardId(1L)
            .maskedCardNumber("4111****1111")
            .balance(BigDecimal.valueOf(500.00))
            .isBlocked(false)
            .isNew(true) // Обязательно true, чтобы R2DBC сделал INSERT, а не UPDATE
            .build();

        Mono<CreditCardCqrsRead> setupAndFind = repository.save(card)
            .then(repository.findByMaskedCardNumber("4111****1111"));

        StepVerifier.create(setupAndFind)
            .expectNextMatches(found -> found.getMaskedCardNumber().equals("4111****1111"))
            .verifyComplete();
    }
}

