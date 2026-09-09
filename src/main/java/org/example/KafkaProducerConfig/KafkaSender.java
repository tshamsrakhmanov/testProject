package org.example.KafkaProducerConfig;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaSender {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(String messageBody, String messageKey, Map<String, String> messageHeaders, String TOPIC,
      String traceId) {
    try {
      kafkaTemplate.send(messageBuilder(messageBody, messageKey, messageHeaders, TOPIC));
      log.info("{} | Message sent successfully", traceId);
    } catch (Exception e) {
      log.error("{} | Failed to send message", traceId);
      throw new RuntimeException("Failed to send Kafka message", e);
    }
  }

  private ProducerRecord<String, String> messageBuilder(
      String messageBody, String messageKey, Map<String, String> messageHeaders, String TOPIC) {

    ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC, messageKey, messageBody);

    if (messageHeaders == null || messageHeaders.isEmpty()) {
      return record;
    }

    for (Map.Entry<String, String> entry : messageHeaders.entrySet()) {
      record.headers().add(entry.getKey(), entry.getValue().getBytes());
    }

    return record;

  }

}
