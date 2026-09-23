package org.example.Config;

public final class AnnotationsParametersConfig {

  public static final String KAFKA_TOPIC_IN = "${custom.kafka_topic_in}";
  public static final String KAFKA_TOPIC_OUT = "${custom.kafka_topic_out}";
  public static final String KAFKA_CONSUMER_ID = "${custom.kafka_topic_listen_consumerGroup}";
  public static final String INTERVAL_CLEANUP = "${custom.interval_ms}";

  private AnnotationsParametersConfig() {
    // utility class — no instances
  }
}
