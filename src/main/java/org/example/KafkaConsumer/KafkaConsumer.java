package org.example.KafkaConsumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

  @Value("${custom.kafka_topic_listen}")
  private String topicIn;

  @Value("custom.kafka_topic_write")
  private String topicOut;

  @KafkaListener(topics = "${custom.kafka_topic_listen}", groupId = "stubConsumerGroupIn")
  private void topicInListener(
      @Payload String messageBody,
      @Header("kafka_receivedMessageKey") String messageKey) {

    log.info("Topic: {}", topicIn);
    log.info("Message: {}", messageBody);

  }

  @KafkaListener(topics = "${custom.kafka_topic_write}", groupId = "stubConsumerGroupOut")
  private void topicOutListener(
      @Payload String messageBody,
      @Header("kafka_receivedMessageKey") String messageKey) {

    log.info("Topic: {}", topicOut);
    log.info("Message: {}", messageBody);

  }
}
