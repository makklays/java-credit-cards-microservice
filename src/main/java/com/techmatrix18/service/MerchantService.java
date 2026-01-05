package com.techmatrix18.service;

import com.techmatrix18.model.Merchant;
import com.techmatrix18.repository.MerchantRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service class for managing merchants.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.12.2025
 */
@Service
public class MerchantService {

    private final MerchantRepository merchantRepo;

    public MerchantService(MerchantRepository merchantRepo) {
        this.merchantRepo = merchantRepo;
    }

    public Mono<Merchant> findById(Long id) { return merchantRepo.findById(id); }

    public Flux<Merchant> findAll() { return merchantRepo.findAll(); }

    public Flux<Merchant> findByCategory(String category) { return merchantRepo.findByCategory(category); }

    public Flux<Merchant> findByTitle(String title) { return merchantRepo.findByTitle(title); }

    public Flux<Merchant> findByRatingGreaterThanEqual(Double rating) {
        return merchantRepo.findByRatingGreaterThanEqual(rating);
    }

    public Flux<Merchant> findByLocationContaining(String location) {
        return merchantRepo.findByLocationContaining(location);
    }
}

