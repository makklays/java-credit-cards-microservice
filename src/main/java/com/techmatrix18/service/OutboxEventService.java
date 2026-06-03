package com.techmatrix18.service;

import com.techmatrix18.model.OutboxEvent;
import com.techmatrix18.repository.OutboxEventRepository;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.LocalDateTime;

/**
 * Service for processing outbox events.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @since 03.06.2026
 * @version 0.0.2
 */
@Service
public class OutboxEventService {

    private final OutboxEventRepository outboxRepo;
    private final ReactiveKafkaProducerTemplate<String, String> kafkaTemplate;
    private final TransactionalOperator txOperator;

    // Внедряем зависимости через конструктор
    public OutboxEventService(OutboxEventRepository outboxRepo,
                              ReactiveKafkaProducerTemplate<String, String> kafkaTemplate,
                              TransactionalOperator txOperator) {
        this.outboxRepo = outboxRepo;
        this.kafkaTemplate = kafkaTemplate;
        this.txOperator = txOperator;
    }

    /**
     * Фоновый процесс, запускающийся каждые 500 миллисекунд.
     * fixedDelay гарантирует, что следующий запуск начнется только после завершения предыдущего.
     */
    @Scheduled(fixedDelay = 500)
    public void processOutboxEvents() {
        outboxRepo.findUnprocessedEvents(50) // Забираем пачку из 50 неотправленных событий
            .flatMap(this::sendToKafkaAndMarkProcessed)
            .subscribeOn(Schedulers.boundedElastic()) // Выполняем в правильном пуле потоков
            .subscribe(); // Активируем реактивную цепочку
    }

    /**
     * Шаг 1: Отправляем сообщение в Kafka.
     * Шаг 2: В случае успеха обновляем событие в БД в рамках транзакции.
     */
    private Mono<Void> sendToKafkaAndMarkProcessed(OutboxEvent event) {
        String topic = determineTopic(event.getAggregateType());

        // 1. Сначала отправляем в Kafka (вне транзакции БД)
        return kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload())
            .flatMap(senderResult -> {
                // 2. Если Kafka ответила успехом, запускаем изолированную транзакцию для БД
                event.setProcessed(true);
                event.setProcessedAt(LocalDateTime.now());

                return outboxRepo.save(event)
                        .as(txOperator::transactional); // Транзакция строго вокруг операции БД
            })
            .onErrorResume(e -> {
                // Сюда прилетит ошибка как от Kafka, так и от БД
                System.err.println("Failed to process outbox event: " + event.getId() + ". Error: " + e.getMessage());
                return Mono.empty(); // Переходим к следующему событию
            })
            .then();
    }

    /**
     * Простая маршрутизация по топикам Kafka в зависимости от типа сущности
     */
    private String determineTopic(String aggregateType) {
        if ("CreditCard".equalsIgnoreCase(aggregateType)) {
            return "credit-cards-events";
        }
        // Здесь можно добавить другие сущности:
        // if ("BankAccount".equalsIgnoreCase(aggregateType)) return "bank-accounts-events";

        return "default-outbox-events";
    }
}

