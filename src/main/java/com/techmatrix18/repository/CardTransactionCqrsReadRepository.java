package com.techmatrix18.repository;

import com.techmatrix18.model.CardTransactionCqrsRead;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Repository interface for managing CardTransactionCqrsRead entities in a CQRS architecture.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @since 04.06.2026
 * @version 0.0.1
 */
@Repository
public interface CardTransactionCqrsReadRepository extends ReactiveCrudRepository<CardTransactionCqrsRead, Long> {

    /**
     * Получить историю транзакций по ID карты с сортировкой по умолчанию (от новых к старым)
     * и поддержкой пагинации (размера страницы и смещения).
     *
     * @param creditCardId ID кредитной карты
     * @param pageable     объект пагинации (PageRequest.of(page, size))
     * @return Flux с порцией денормализованных транзакций
     */
    Flux<CardTransactionCqrsRead> findByCreditCardIdOrderByCreatedAtDesc(Long creditCardId, Pageable pageable);

    /**
     * Альтернативный быстрый метод получения последних N транзакций без использования объекта Pageable.
     * Этот запрос гарантированно использует индекс (credit_card_id, created_at DESC).
     *
     * @param creditCardId ID кредитной карты
     * @param limit        сколько транзакций выгрузить (например, последние 10 для главного экрана)
     * @return Flux с последними транзакциями
     */
    @Query("SELECT * FROM card_transactions_cqrs_read WHERE credit_card_id = :creditCardId ORDER BY created_at DESC LIMIT :limit")
    Flux<CardTransactionCqrsRead> findLatestTransactions(Long creditCardId, int limit);
}

