package org.example.InternalDataBase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.Config.AnnotationsParametersConfig;
import org.example.Config.ApplicationProperties;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoCleanService {

  public static final String TRACE_ID = "traceId";
  private final DataBaseInterface dataBaseInterface;
  private final ApplicationProperties applicationProperties;

  @Scheduled(fixedDelayString = AnnotationsParametersConfig.INTERVAL_CLEANUP)
  public void cleanOldMessages() {

    MDC.put(TRACE_ID, UUID.randomUUID().toString().replace("-", ""));
    try {
      LocalDateTime threshold = LocalDateTime.now().minusMinutes(applicationProperties.retentionMinutes());
      int deleted = dataBaseInterface.deleteOlderThan(threshold);
      if (deleted > 0) {
        log.info("Removed {} messages", deleted);
      } else {
        log.info("Nothing to delete");
      }
    } catch (Exception e) {
      log.error("Auto-cleaner failed", e);
    } finally {
      MDC.remove(TRACE_ID); // ← always clean up, scheduler threads are reused
    }

  }
}
