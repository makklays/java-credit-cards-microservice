package com.techmatrix18.repository;

import com.techmatrix18.model.CreditCardIdempotent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for handling Idempotency Keys to prevent duplicate transactions.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.06.2026
 */
@Repository
public interface CreditCardIdempotentRepository extends ReactiveCrudRepository<CreditCardIdempotent, String> {
    // Базовых методов save() и existsById() из ReactiveCrudRepository нам будет полностью достаточно
}

