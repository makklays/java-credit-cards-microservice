package com.techmatrix18.service;

import com.techmatrix18.model.LoyaltyPoint;
import com.techmatrix18.repository.LoyaltyRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Service class for managing loyalty programs.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.12.2025
 */
@Service
public class LoyaltyService {
    private final LoyaltyRepository loyaltyRepo;

    public LoyaltyService(LoyaltyRepository loyaltyRepo) {
        this.loyaltyRepo = loyaltyRepo;
    }

    public Flux<LoyaltyPoint> findAll() {
        return loyaltyRepo.findAll();
    }

    public Mono<LoyaltyPoint> findByCreditCardId(Long creditCardId) {
        return loyaltyRepo.findByCreditCardId(creditCardId);
    }

    public Flux<LoyaltyPoint> findByPointsBalanceGreaterThanEqual(Long points) {
        return loyaltyRepo.findByPointsBalanceGreaterThanEqual(points);
    }

    public Flux<LoyaltyPoint> findByCreditCardIdAndExpirationDateLessThan(Long creditCardId, java.time.LocalDateTime dateTime) {
        return loyaltyRepo.findByCreditCardIdAndExpirationDateLessThan(creditCardId, dateTime);
    }

    public Flux<LoyaltyPoint> findByExpirationDateBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        return loyaltyRepo.findByExpirationDateBetween(startDate, endDate);
    }

    public Flux<LoyaltyPoint> findByPointsBalanceLessThan(Long points) {
        return loyaltyRepo.findByPointsBalanceLessThan(points);
    }

    public Flux<LoyaltyPoint> findByCreditCardIdIn(Set<Long> creditCardIds) {
        return loyaltyRepo.findByCreditCardIdIn(creditCardIds);
    }

    public Flux<LoyaltyPoint> findByLastUpdatedAfter(java.time.LocalDateTime dateTime) {
        return loyaltyRepo.findByLastUpdatedAfter(dateTime);
    }
}

