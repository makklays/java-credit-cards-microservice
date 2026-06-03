package com.techmatrix18.controller;

import com.techmatrix18.model.LoyaltyPoint;
import com.techmatrix18.model.LoyaltyPointCqrsRead;
import com.techmatrix18.service.LoyaltyPointCommandService;
import com.techmatrix18.service.LoyaltyPointQueryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * REST-контроллер для управления бонусными баллами лояльности.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 04.06.2026
 */
@RestController
@RequestMapping(path = "/api/v1/loyalty", produces = "application/json")
@CrossOrigin(origins = "*")
public class LoyaltyPointController {

    private final LoyaltyPointCommandService loyaltyCommandService;
    private final LoyaltyPointQueryService loyaltyQueryService;

    public LoyaltyPointController(LoyaltyPointCommandService loyaltyCommandService,
                                  LoyaltyPointQueryService loyaltyQueryService) {
        this.loyaltyCommandService = loyaltyCommandService;
        this.loyaltyQueryService = loyaltyQueryService;
    }

    // =========================================================================
    //   CQRS: ВЕТКА ЧТЕНИЯ (QUERIES) -> Чтение из loyalty_points_cqrs_read
    // =========================================================================

    /**
     * Получить актуальный баланс бонусов по ID кредитной карты.
     * Эндпоинт оптимизирован для быстрого рендеринга баланса на главном экране.
     */
    @GetMapping("/balance/{creditCardId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<LoyaltyPointCqrsRead> getBalance(@PathVariable("creditCardId") Long creditCardId) {
        return loyaltyQueryService.getBalanceByCardId(creditCardId);
    }

    // =========================================================================
    //   CQRS + ИДЕМПОТЕНТНОСТЬ: ВЕТКА ЗАПИСИ (COMMANDS) -> Через Outbox
    // =========================================================================

    /**
     * Операция начисления баллов лояльности (Идемпотентная)
     */
    @PostMapping("/earn")
    @ResponseStatus(HttpStatus.OK)
    public Mono<LoyaltyPoint> earnPoints(
            @RequestHeader("X-Idempotency-Key") String idempotencyKey,
            @RequestParam("credit-card") Long creditCardId,
            @RequestParam("points") Long points,
            @RequestParam(value = "expiration-date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expirationDate) {

        if (points == null || points <= 0) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Points to earn must be positive"));
        }
        return loyaltyCommandService.earnPoints(idempotencyKey, creditCardId, points, expirationDate);
    }

    /**
     * Операция списания/траты баллов лояльности (Идемпотентная)
     */
    @PostMapping("/spend")
    @ResponseStatus(HttpStatus.OK)
    public Mono<LoyaltyPoint> spendPoints(
            @RequestHeader("X-Idempotency-Key") String idempotencyKey,
            @RequestParam("credit-card") Long creditCardId,
            @RequestParam("points") Long points) {

        if (points == null || points <= 0) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Points to spend must be positive"));
        }
        return loyaltyCommandService.spendPoints(idempotencyKey, creditCardId, points);
    }
}

