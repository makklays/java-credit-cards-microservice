package com.techmatrix18.service;

import com.techmatrix18.model.CardTransaction;
import com.techmatrix18.repository.CardTransactionRepository;
import com.techmatrix18.repository.CreditCardRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service class for managing CardTransaction entities.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 05.01.2026
 */
@Service
public class CardTransactionService {

    private final CreditCardRepository creditCardRepo;

    private final CardTransactionRepository cardTransactionRepo;

    public CardTransactionService(CreditCardRepository creditCardRepo, CardTransactionRepository cardTransactionRepo) {
        this.creditCardRepo = creditCardRepo;
        this.cardTransactionRepo = cardTransactionRepo;
    }

    Flux<CardTransaction> findByCreditCardId(Long creditCardId) {
        return cardTransactionRepo.findByCreditCardId(creditCardId);
    }

    Mono<CardTransaction> findByIdAndCreditCardId(Long id, Long creditCardId) {
        return cardTransactionRepo.findByIdAndCreditCardId(id, creditCardId);
    }

    Flux<CardTransaction> findByOperationType(String operationType) {
        return cardTransactionRepo.findByOperationType(operationType);
    }

    Flux<CardTransaction> findByStatus(String status) {
        return cardTransactionRepo.findByStatus(status);
    }
}

