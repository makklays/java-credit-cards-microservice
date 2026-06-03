package com.techmatrix18.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.model.LoyaltyPoint;
import com.techmatrix18.model.LoyaltyPointCqrsRead;
import com.techmatrix18.repository.LoyaltyPointCqrsReadRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

/**
 * Компонент для потребления событий о баллах лояльности из Kafka и обновления CQRS Read-витрины.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 04.06.2026
 */
@Component
public class LoyaltyPointEventConsumer {

    private final ReactiveKafkaConsumerTemplate<String, String> kafkaConsumerTemplate;
    private final LoyaltyPointCqrsReadRepository cqrsReadRepo;
    private final ObjectMapper objectMapper;

    public LoyaltyPointEventConsumer(ReactiveKafkaConsumerTemplate<String, String> kafkaConsumerTemplate,
                                      LoyaltyPointCqrsReadRepository cqrsReadRepo,
                                      ObjectMapper objectMapper) {
        this.kafkaConsumerTemplate = kafkaConsumerTemplate;
        this.cqrsReadRepo = cqrsReadRepo;
        this.objectMapper = objectMapper;
    }

    /**
     * Метод автоматически запускается при старте приложения
     * и открывает бесконечный реактивный поток чтения бонусных событий из Kafka.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void startConsuming() {
        kafkaConsumerTemplate.receiveAutoAck() // Читаем с авто-подтверждением (Auto Commit)
            // Изолируем обработку каждого сообщения внутри flatMap, чтобы поток Кафки никогда не умирал
            .flatMap(record -> processRecord(record)
                .onErrorResume(error -> {
                    System.err.println("Error processing loyalty message from partition " + record.partition() + ": " + error.getMessage());
                    // Возвращаем пустой Mono, чтобы корневой поток Kafka перешел к следующему сообщению
                    return Mono.empty();
                })
            )
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(
                null,
                error -> System.err.println("Critical unhandled error in Loyalty Kafka Consumer stream: " + error.getMessage())
            );
    }

    /**
     * Обработка одного сообщения из Kafka
     */
    private Mono<Void> processRecord(ConsumerRecord<String, String> record) {
        return Mono.fromCallable(() -> objectMapper.readValue(record.value(), LoyaltyPoint.class))
            .flatMap(this::updateCqrsReadView)
            .then(); // Превращаем в Mono<Void> для реактивной цепочки
    }

    /**
     * Метод обновления CQRS Read-витрины баллов лояльности
     */
    private Mono<LoyaltyPointCqrsRead> updateCqrsReadView(LoyaltyPoint writeModel) {
        return cqrsReadRepo.findById(writeModel.getCreditCardId())
            .defaultIfEmpty(new LoyaltyPointCqrsRead()) // Если записи для этой карты в Read-БД еще нет
            .flatMap(readModel -> {
                if (readModel.getCreditCardId() == null) {
                    readModel.setCreditCardId(writeModel.getCreditCardId());
                    readModel.setNew(true); // Для Spring Data R2DBC делать INSERT
                } else {
                    readModel.setNew(false); // Для Spring Data R2DBC делать UPDATE
                }

                // Синхронизируем поля из события Кафки
                readModel.setPointsBalance(writeModel.getPointsBalance());
                readModel.setExpirationDate(writeModel.getExpirationDate());
                readModel.setUpdatedAt(LocalDateTime.now());

                return cqrsReadRepo.save(readModel);
            });
    }
}

