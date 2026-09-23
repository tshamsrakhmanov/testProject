package org.example.KafkaConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.example.Config.AnnotationsParametersConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

  @KafkaListener(topics = { AnnotationsParametersConfig.KAFKA_TOPIC_IN,
      AnnotationsParametersConfig.KAFKA_TOPIC_OUT }, groupId = AnnotationsParametersConfig.KAFKA_CONSUMER_ID)
  private void topicOutListener(ConsumerRecord<String, String> record) {
    log.info(" <---");
    log.info("Kafka message RECEIVED");
    log.info("Topic: {}, partition: {}, offset: {}",
        record.topic(), record.partition(), record.offset());
    log.info("Body: {}, Key: {}, Headers: {}", record.value(), record.key(), kafkaHeadersToString(record.headers()));

  }

  private static String kafkaHeadersToString(
      org.apache.kafka.common.header.Headers headers) {
    if (headers == null)
      return "[]";
    StringBuilder sb = new StringBuilder("[");
    boolean first = true;
    for (org.apache.kafka.common.header.Header h : headers) {
      if (!first)
        sb.append(", ");
      first = false;
      String value = h.value() == null
          ? "null"
          : new String(h.value(), java.nio.charset.StandardCharsets.UTF_8);
      sb.append(h.key()).append('=').append(value);
    }
    return sb.append(']').toString();
  }
}
