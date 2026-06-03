package com.techmatrix18.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.model.CardTransaction;
import com.techmatrix18.model.CardTransactionCqrsRead;
import com.techmatrix18.repository.CardTransactionCqrsReadRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * CardTransactionEventConsumer is a Spring component responsible for consuming card transaction events from a Kafka topic.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @since 04.06.2026
 * @version 0.0.1
 */
@Component
public class CardTransactionEventConsumer {

    private final ReactiveKafkaConsumerTemplate<String, String> kafkaConsumerTemplate;
    private final CardTransactionCqrsReadRepository cqrsReadRepo;
    private final ObjectMapper objectMapper;

    public CardTransactionEventConsumer(ReactiveKafkaConsumerTemplate<String, String> kafkaConsumerTemplate,
                                        CardTransactionCqrsReadRepository cqrsReadRepo,
                                        ObjectMapper objectMapper) {
        this.kafkaConsumerTemplate = kafkaConsumerTemplate;
        this.cqrsReadRepo = cqrsReadRepo;
        this.objectMapper = objectMapper;
    }

    /**
     * Автоматический запуск бесконечного реактивного потока чтения транзакций из Kafka при старте приложения.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void startConsuming() {
        kafkaConsumerTemplate.receiveAutoAck()
            // Изолируем обработку каждой транзакции внутри flatMap, защищая корневой поток Kafka от падения
            .flatMap(record -> processRecord(record)
                .onErrorResume(error -> {
                    System.err.println("Error processing transaction message from partition " + record.partition() + ": " + error.getMessage());
                    return Mono.empty(); // Переходим к следующему сообщению в Kafka
                })
            )
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(
                null,
                error -> System.err.println("Critical unhandled error in Transaction Kafka Consumer stream: " + error.getMessage())
            );
    }

    /**
     * Десериализация сообщения и запуск сохранения в Read-витрину
     */
    private Mono<Void> processRecord(ConsumerRecord<String, String> record) {
        return Mono.fromCallable(() -> objectMapper.readValue(record.value(), CardTransaction.class))
            .flatMap(this::saveToCqrsReadView)
            .then();
    }

    /**
     * Прямой INSERT транзакции в CQRS Read-таблицу.
     * Так как история транзакций неизменяема (Append-Only), мы сразу делаем быстрый INSERT.
     */
    private Mono<CardTransactionCqrsRead> saveToCqrsReadView(CardTransaction writeModel) {
        CardTransactionCqrsRead readModel = new CardTransactionCqrsRead();

        // Мапим поля из основной модели в денормализованную Read-модель
        readModel.setTransactionId(writeModel.getId());
        readModel.setCreditCardId(writeModel.getCreditCardId());
        readModel.setMerchantId(writeModel.getMerchantId());
        readModel.setOperationType(writeModel.getOperationType());
        readModel.setAmount(writeModel.getAmount());
        readModel.setFeeAmount(writeModel.getFeeAmount());
        readModel.setLoyaltyPoints(writeModel.getLoyaltyPoints());
        readModel.setCurrencyCode(writeModel.getCurrencyCode());
        readModel.setStatus(writeModel.getStatus());
        readModel.setCreatedAt(writeModel.getCreatedAt());
        readModel.setUpdatedAt(writeModel.getUpdatedAt());

        // Указываем Spring Data R2DBC делать строго INSERT (так как ключ не автоинкрементный для этой таблицы, а берется из Write-модели)
        readModel.setNew(true);

        return cqrsReadRepo.save(readModel);
    }
}

