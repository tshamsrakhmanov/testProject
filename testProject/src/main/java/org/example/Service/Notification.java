package org.example.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Notification {

  @EventListener(ApplicationReadyEvent.class)
  public void notification() {
    log.info("---");
    log.info("SOME NEW LINE");
    log.info("---");
  }
}
