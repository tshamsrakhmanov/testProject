package org.example.KafkaConsumerConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerStartUpVerifier {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ConfigurableApplicationContext context;

    @Value("${custom.kafka_topic_write}")
    private String topicWrite;

    @EventListener(ApplicationReadyEvent.class)
    public void validate() {
        String traceId_string = UUID.randomUUID().toString().replace("-", "");
        log.info("{} | Some info", traceId_string);
        try {
            kafkaTemplate.getProducerFactory().createProducer().metrics();
            log.info("{} | Connect OK", traceId_string);
        } catch (Exception e ) {
            log.error("{} | Error connect: {}", traceId_string, e.toString());
            log.error("{} | {}\n\n{}", traceId_string, e.getCause(), e.getMessage());
            int exitCode = SpringApplication.exit(context, () -> 1);
            System.exit(exitCode);
        }
    }
}