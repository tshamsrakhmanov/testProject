package org.example.KafkaConsumerConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaStartupVerifier {

  private final ConfigurableApplicationContext context;

  @Value("${custom.kafka_brokers}")
  private String bootstrapServers;

  @Value("${custom.kafka_topic_write}")
  private String topicWrite;

  @EventListener(ApplicationReadyEvent.class)
  public void verifyKafkaConnection() {
    log.info("Checking Kafka connection to: {}", bootstrapServers);

    Map<String, Object> config = new HashMap<>();
    config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    config.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 2000);
    config.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, 2000);
    config.put(AdminClientConfig.RETRIES_CONFIG, 0);

    try (AdminClient adminClient = AdminClient.create(config)) {
      adminClient.describeCluster()
          .clusterId()
          .get(2, TimeUnit.SECONDS);

      log.info("✅ Kafka connection successful");
    } catch (Exception e) {
      log.error("❌ Kafka connection failed: {}", e.getMessage());
      SpringApplication.exit(context, () -> 1);
      System.exit(1);
    }
  }
}
