package com.techmatrix18.repository;

import com.techmatrix18.model.OutboxEvent;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import java.util.UUID;

/**
 * Reactive repository for OutboxEvent entities.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @since 03.06.2026
 * @version 0.0.2
 */
@Repository
public interface OutboxEventRepository extends ReactiveCrudRepository<OutboxEvent, UUID> {

    /**
     * Finds a batch of unprocessed events ordered by creation time.
     * This query uses the partial index 'idx_outbox_unprocessed' for instant lookups.
     *
     * @param limit maximum number of events to fetch in one batch
     * @return Flux of unprocessed outbox events
     */
    @Query("SELECT * FROM outbox_events WHERE processed = false ORDER BY created_at ASC LIMIT :limit")
    Flux<OutboxEvent> findUnprocessedEvents(int limit);
}

