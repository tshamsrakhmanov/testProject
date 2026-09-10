package org.example.KafkaProducerConfig;

import java.util.concurrent.Executor;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

  @Bean(name = "kafkaExecutor")
  public Executor kafkaExecutor() {
    ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
    exec.setCorePoolSize(4);
    exec.setMaxPoolSize(16);
    exec.setQueueCapacity(500);
    exec.setThreadNamePrefix("kafka-send-");
    exec.setTaskDecorator(new MdcTaskDecorator()); // belt-and-suspenders MDC propagation
    exec.initialize();
    return exec;
  }

  /** Copies MDC from the submitting thread to the worker thread. */
  static class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
      Map<String, String> context = MDC.getCopyOfContextMap();
      return () -> {
        if (context != null)
          MDC.setContextMap(context);
        try {
          runnable.run();
        } finally {
          MDC.clear();
        }
      };
    }
  }
}
