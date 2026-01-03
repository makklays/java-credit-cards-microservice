package com.techmatrix18.repository;

import com.techmatrix18.model.CreditCard;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Repository interface for CreditCard entities.
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
public interface CreditCardRepository extends ReactiveCrudRepository<CreditCard, Long> {

    Flux<CreditCard> findByCardholderName(String cardholderName);

    @Query("SELECT * FROM credit_cards WHERE balance > :minBalance")
    Flux<CreditCard> findRichAccounts(BigDecimal minBalance);

    Flux<CreditCard> findByBalanceBetween(BigDecimal minBalance, BigDecimal maxBalance);

    Flux<CreditCard> findByIsBlocked(Boolean isBlocked);

    Flux<CreditCard> findByCardholderNameAndIsBlocked(String cardholderName, Boolean isBlocked);

    Flux<CreditCard> findByBalanceLessThan(BigDecimal balance);

    Flux<CreditCard> findByBalanceGreaterThanEqual(BigDecimal balance);

    Mono<CreditCard> findByCardNumber(String cardNumber);

    Mono<CreditCard> findByCardNumberAndCardholderName(String cardNumber, String cardholderName);

    Mono<CreditCard> findByCardNumberAndIsBlocked(String cardNumber, Boolean isBlocked);

    Mono<CreditCard> findByCardholderNameAndCardNumberAndIsBlocked(String cardholderName, String cardNumber, Boolean isBlocked);
}

