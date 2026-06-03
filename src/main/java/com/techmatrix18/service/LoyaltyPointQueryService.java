package com.techmatrix18.service;

import com.techmatrix18.model.LoyaltyPointCqrsRead;
import com.techmatrix18.repository.LoyaltyPointCqrsReadRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service class for handling queries related to loyalty points.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 04.06.2026
 */
@Service
public class LoyaltyPointQueryService {

    private final LoyaltyPointCqrsReadRepository lpCqrsReadRepo;

    // Внедряем только репозиторий Read-модели
    public LoyaltyPointQueryService(LoyaltyPointCqrsReadRepository lpCqrsReadRepo) {
        this.lpCqrsReadRepo = lpCqrsReadRepo;
    }

    /**
     * Получить баланс бонусов по ID кредитной карты.
     * Используется для вывода баллов на главный экран или в личный кабинет.
     *
     * @param creditCardId ID кредитной карты
     * @return Mono с денормализованными данными баллов лояльности
     */
    public Mono<LoyaltyPointCqrsRead> getBalanceByCardId(Long creditCardId) {
        return lpCqrsReadRepo.findById(creditCardId)
            // Если в read-таблице нет записи для этой карты, возвращаем 404 Not Found
            .switchIfEmpty(Mono.error(new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Loyalty points balance not found for card ID: " + creditCardId
            )));
    }

    /**
     * Получить список всех бонусных балансов.
     * Может использоваться в админ-панелях или внутренних аналитических сервисах.
     *
     * @return Flux со всеми записями из read-витрины
     */
    public Flux<LoyaltyPointCqrsRead> getAllBalances() {
        return lpCqrsReadRepo.findAll();
    }
}

