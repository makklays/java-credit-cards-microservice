package com.techmatrix18.repository;

import com.techmatrix18.model.LoyaltyPointIdempotency;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing LoyaltyPointIdempotency entities.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @since 03.06.2026
 * @version 0.0.1
 */
@Repository
public interface LoyaltyPointIdempotencyRepository extends ReactiveCrudRepository<LoyaltyPointIdempotency, String> {
    //
}
