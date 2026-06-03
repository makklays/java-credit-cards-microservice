package com.techmatrix18.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.model.CreditCard;
import com.techmatrix18.model.CreditCardCqrsRead;
import com.techmatrix18.repository.CreditCardCqrsReadRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

/**
 * Kafka Consumer для обработки событий из топика "credit-card-events".
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 03.06.2026
 */
@Component
public class CreditCardEventConsumer {

    private final ReactiveKafkaConsumerTemplate<String, String> kafkaConsumerTemplate;
    private final CreditCardCqrsReadRepository cqrsReadRepo;
    private final ObjectMapper objectMapper;

    public CreditCardEventConsumer(ReactiveKafkaConsumerTemplate<String, String> kafkaConsumerTemplate,
                                   CreditCardCqrsReadRepository cqrsReadRepo,
                                   ObjectMapper objectMapper) {
        this.kafkaConsumerTemplate = kafkaConsumerTemplate;
        this.cqrsReadRepo = cqrsReadRepo;
        this.objectMapper = objectMapper;
    }

    /**
     * Метод автоматически запускается при старте приложения,
     * открывает бесконечный реактивный поток чтения из Kafka.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void startConsuming() {
        kafkaConsumerTemplate.receiveAutoAck() // Читаем с авто-подтверждением (Auto Commit)
            // Изолируем обработку каждого сообщения внутри flatMap
            .flatMap(record -> processRecord(record)
                .onErrorResume(error -> {
                    // Перехватываем абсолютно любые ошибки (JSON, R2DBC, DB, Network)
                    System.err.println("Error processing message from partition " + record.partition() + ": " + error.getMessage());
                    // Возвращаем пустой Mono, чтобы корневой поток Kafka перешел к следующему сообщению
                    return Mono.empty();
                })
            )
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(
                null,
                error -> System.err.println("Critical unhandled error in Kafka Consumer stream: " + error.getMessage())
            );
    }

    /**
     * Обработка одного сообщения из Kafka
     */
    private Mono<Void> processRecord(ConsumerRecord<String, String> record) {
        return Mono.fromCallable(() -> objectMapper.readValue(record.value(), CreditCard.class))
            .flatMap(this::updateCqrsReadView)
            .then(); // Сбрасываем результат CreditCardCqrsRead, превращая в Mono<Void>
    }

    /**
     * Метод обновления CQRS Read-витрины
     */
    private Mono<CreditCardCqrsRead> updateCqrsReadView(CreditCard writeModel) {
        return cqrsReadRepo.findById(writeModel.getId())
            .defaultIfEmpty(new CreditCardCqrsRead()) // Если карты в Read-БД еще нет
            .flatMap(readModel -> {
                if (readModel.getCardId() == null) {
                    readModel.setCardId(writeModel.getId());
                    readModel.setUserId(writeModel.getUserId());
                    readModel.setMaskedCardNumber(maskCardNumber(writeModel.getCardNumber()));
                    readModel.setNew(true); // Для R2DBC делать INSERT
                } else {
                    readModel.setNew(false); // Для R2DBC делать UPDATE
                }

                // Наполняем read-модель актуальными данными из события Кафки
                readModel.setBalance(writeModel.getBalance());
                readModel.setCreditLimit(writeModel.getCreditLimit());
                readModel.setCurrencyCode(writeModel.getCurrencyCode());
                readModel.setStatus(writeModel.getStatus());
                readModel.setType(writeModel.getType());
                readModel.setBankName(writeModel.getBankName());
                readModel.setIsBlocked(writeModel.getIsBlocked());
                readModel.setUpdatedAt(LocalDateTime.now());

                return cqrsReadRepo.save(readModel);
            });
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "****";
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}

