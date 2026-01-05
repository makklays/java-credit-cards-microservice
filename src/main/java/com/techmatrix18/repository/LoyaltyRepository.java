package com.techmatrix18.repository;

import com.techmatrix18.model.LoyaltyPoint;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Repository interface for managing LoyaltyPoint entities.
 *
 * save(S entity)	                            Mono<T>	Сохраняет объект (insert или update)
 * saveAll(Iterable<S> entities)	            Flux<S>	Сохраняет несколько объектов
 * saveAll(Publisher<S> entityStream)	        Flux<S>	Сохраняет поток объектов (reactive)
 * findById(ID id)	                            Mono<T>	Найти объект по id
 * existsById(ID id)	                        Mono<Boolean>	Проверить, существует ли объект по id
 * findAll()	                                Flux<T>	Найти все объекты
 * findAllById(Iterable<ID> ids)	            Flux<T>	Найти объекты по списку id
 * findAll(Sort sort)	                        Flux<T>	Найти все объекты с сортировкой
 * count()	                                    Mono<Long>	Количество объектов в репозитории
 * deleteById(ID id)	                        Mono<Void>	Удалить объект по id
 * delete(T entity)	                            Mono<Void>	Удалить объект
 * deleteAll(Iterable<? extends T> entities)	Mono<Void>	Удалить список объектов
 * deleteAll()	                                Mono<Void>	Удалить все объекты
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.12.2025
 */
@Repository
public interface LoyaltyRepository extends ReactiveCrudRepository<LoyaltyPoint, Long> {

    Mono<LoyaltyPoint> findByCreditCardId(Long creditCardId);

    Flux<LoyaltyPoint> findByPointsBalanceGreaterThanEqual(Long points);

    Flux<LoyaltyPoint> findByCreditCardIdAndExpirationDateLessThan(Long creditCardId, java.time.LocalDateTime dateTime);

    Flux<LoyaltyPoint> findByExpirationDateBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);

    Flux<LoyaltyPoint> findByPointsBalanceLessThan(Long points);

    Flux<LoyaltyPoint> findByCreditCardIdIn(Set<Long> creditCardIds);

    Flux<LoyaltyPoint> findByLastUpdatedAfter(java.time.LocalDateTime dateTime);
}

