package org.example.Interceptors;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.MDC;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.stereotype.Component;

@Component
public class KafkaMdcRecordInterceptor implements RecordInterceptor<String, String> {

  @Override
  public ConsumerRecord<String, String> intercept(ConsumerRecord<String, String> record,
      Consumer<String, String> consumer) {
    String traceId = null;
    Header header = record.headers().lastHeader("traceId");
    if (header != null && header.value() != null) {
      traceId = new String(header.value(), StandardCharsets.UTF_8);
    }
    if (traceId == null || traceId.isBlank()) {
      traceId = UUID.randomUUID().toString();
    }
    MDC.put("traceId", traceId);
    return record;
  }

  @Override
  public void afterRecord(ConsumerRecord<String, String> record,
      Consumer<String, String> consumer) {
    MDC.clear();
  }

}
