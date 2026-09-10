package org.example.KafkaProducerConfig;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaSender {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final Executor kafkaExecutor;

  public CompletableFuture<Void> sendMessageAsync(String messageBody,
      String messageKey,
      Map<String, String> messageHeaders,
      String TOPIC,
      long delayMillis) {

    // Capture MDC on the caller thread
    Map<String, String> mdcContext = MDC.getCopyOfContextMap();

    return CompletableFuture.runAsync(() -> {
      // Restore MDC on the async thread
      if (mdcContext != null) {
        MDC.setContextMap(mdcContext);
      }
      try {
        if (delayMillis > 0) {
          log.warn("Delay to be applied: {}", delayMillis);
          Thread.sleep(delayMillis);
        }
        // Call the existing sync-ish method (which itself uses whenComplete)
        sendMessage(messageBody, messageKey, messageHeaders, TOPIC);
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        log.warn("Sending is interrupted for topic={}", TOPIC, ie);
      } finally {
        MDC.clear();
      }
    }, kafkaExecutor);
  }

  public void sendMessage(String messageBody, String messageKey, Map<String, String> messageHeaders, String TOPIC) {

    Map<String, String> mdcContext = MDC.getCopyOfContextMap();

    kafkaTemplate.send(messageBuilder(messageBody, messageKey, messageHeaders, TOPIC))
        .whenComplete((result, ex) -> {

          if (mdcContext != null) {
            MDC.setContextMap(mdcContext);
          }

          try {
            if (ex != null) {
              log.error("Failed to send message to topic={}", TOPIC, ex);
              return;
            }
            RecordMetadata md = result.getRecordMetadata();
            log.info("Message send: topic:{} partition:{} offset:{} timestamp:{}",
                md.topic(), md.partition(), md.offset(), md.timestamp());
          } finally {
            // 3. Clean up so we don't leak state into a reused Kafka thread
            MDC.clear();
          }
        });
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
