package com.techmatrix18.sandbox;

import com.techmatrix18.model.Currency;
import com.techmatrix18.repository.CurrencyRepository;
import com.techmatrix18.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the CurrencyService class using Mockito and Reactor Test.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.06.2026
 */
public class CurrencyServiceTest {

    private CurrencyService currencyService;
    private CurrencyRepository currencyRepositoryMock;

    // ФАЗА SET UP: Создаем чистый мок перед каждым тестом
    @BeforeEach
    void setUp() {
        currencyRepositoryMock = mock(CurrencyRepository.class);
        currencyService = new CurrencyService(currencyRepositoryMock);
    }

    @Test
    void shouldFindCurrencyByCc() {
        // --- 1. ARRANGE (Setup данных) ---
        String code = "USD";
        Currency usd = createCurrency("USD", 840, "Доллар США", 41.20f, "25.06.2026");

        // Обучаем мок возвращать Mono с нашей валютой
        when(currencyRepositoryMock.findByCc(code)).thenReturn(Mono.just(usd));

        // --- 2. ACT ---
        Mono<Currency> resultMono = currencyService.findByCc(code);

        // --- 3. ASSERT ---
        StepVerifier.create(resultMono)
            .expectNextMatches(currency ->
                "USD".equals(currency.getCc()) &&
                    Integer.valueOf(840).equals(currency.getR030()) &&
                    Float.valueOf(41.20f).equals(currency.getRate())
            )
            .verifyComplete();

        verify(currencyRepositoryMock, times(1)).findByCc(code);
    }

    @Test
    void shouldFindAllCurrencies() {
        // --- 1. ARRANGE (Setup данных для Flux) ---
        Currency usd = createCurrency("USD", 840, "Доллар США", 41.20f, "25.06.2026");
        Currency eur = createCurrency("EUR", 978, "Евро", 44.50f, "25.06.2026");

        // Реактивный Flux возвращает несколько элементов по очереди
        when(currencyRepositoryMock.findAll()).thenReturn(Flux.just(usd, eur));

        // --- 2. ACT ---
        Flux<Currency> resultFlux = currencyService.findAll();

        // --- 3. ASSERT ---
        StepVerifier.create(resultFlux)
            .expectNext(usd) // Проверяем, что первый элемент равен usd
            .expectNext(eur) // Проверяем, что второй элемент равен eur
            .verifyComplete(); // Убеждаемся, что стрим успешно закрылся

        verify(currencyRepositoryMock, times(1)).findAll();
    }

    // Вспомогательный метод для быстрой сборки объекта Currency внутри теста
    private Currency createCurrency(String cc, Integer r030, String title, Float rate, String date) {
        Currency currency = new Currency();
        currency.setCc(cc);
        currency.setR030(r030);
        currency.setTitle(title);
        currency.setRate(rate);
        currency.setExchangedate(date);
        currency.setCreatedAt(LocalDateTime.now());
        return currency;
    }
}

