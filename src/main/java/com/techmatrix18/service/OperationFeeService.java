package com.techmatrix18.service;

import com.techmatrix18.model.OperationFee;
import com.techmatrix18.repository.OperationFeeRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service class for managing OperationFee entities.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.12.2025
 */
@Service
public class OperationFeeService {
    private final OperationFeeRepository operationFeeRepo;

    public OperationFeeService(OperationFeeRepository operationFeeRepo) {
        this.operationFeeRepo = operationFeeRepo;
    }

    public Flux<OperationFee> findAll() {
        return operationFeeRepo.findAll();
    }

    public Mono<OperationFee> findById(Long id) {
        return operationFeeRepo.findById(id);
    }

    public Mono<OperationFee> findByOperationType(String operationType) {
        return operationFeeRepo.findByOperationType(operationType);
    }
}

