package org.example.Config;

public final class AnnotationsParametersConfig {

  public static final String KAFKA_TOPIC_IN = "${custom.kafka_topic_in}";
  public static final String KAFKA_TOPIC_OUT = "${custom.kafka_topic_out}";
  public static final String KAFKA_CONSUMER_ID = "${custom.kafka_topic_listen_consumerGroup}";
  public static final String CRON_H2_CLEAN_UP = "${custom.cron_h2_clean_up}";
  public static final String CRON_SWIPE_BINDINGS = "${custom.cron_swipe_bindings}";

  private AnnotationsParametersConfig() {
    // utility class — no instances
  }
}
