package org.example.Config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "custom")
public record ApplicationProperties(
    String kafkaTopicIn,
    String kafkaTopicOut,
    String privateKey,
    List<String> kafkaBrokers,
    long retentionMinutes,
    long deliveryDelay,
    String kafkaTopicListenConsumerGroup) {
}
