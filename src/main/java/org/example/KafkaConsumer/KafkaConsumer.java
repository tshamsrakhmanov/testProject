package org.example.KafkaConsumer;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
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

  // @KafkaListener(topics = "${custom.kafka_topic_listen}", groupId =
  // "stubConsumerGroupIn")
  // private void topicInListener(
  // @Payload String messageBody,
  // @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) String
  // messageKey,
  // @Header(name = KafkaHeaders.RECEIVED_TOPIC) String topic,
  // @Header(name = KafkaHeaders.RECEIVED_PARTITION) int partition,
  // @Header(name = KafkaHeaders.OFFSET) long offset,
  // @Header(name = "kafka_receivedHeader_traceId", required = false) byte[]
  // traceId
  //
  // ) {
  //
  // log.info("Topic: {} partition: {} offset: {} key: {}", topic, partition,
  // offset, messageKey);
  // log.info("Message: {}", messageBody);
  // log.info("traceId header: {}", traceId != null ? new String(traceId,
  // StandardCharsets.UTF_8) : null);
  //
  // }

  @KafkaListener(topics = { "${custom.kafka_topic_write}",
      "${custom.kafka_topic_listen}" }, groupId = "stubConsumerGroupOut")
  private void topicOutListener(

      @Payload String messageBody,
      @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) String messageKey,
      @Header(name = KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(name = KafkaHeaders.RECEIVED_PARTITION) int partition,
      @Header(name = KafkaHeaders.OFFSET) long offset

  ) {

    log.info("Kafka message RECEIVED");
    log.info("Topic: {} partition: {} offset: {} key: {}", topic, partition, offset, messageKey);
    log.info("Message: {}", messageBody);

  }
}
