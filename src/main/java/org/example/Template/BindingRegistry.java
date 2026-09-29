package org.example.Template;

import org.example.Config.AnnotationsParametersConfig;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class BindingRegistry {

  public static final String TRACE_ID = "traceId";

  // cache to store link of id <-> template <-> TTL
  // id is used as KEY - to be able to have only one id linkage
  private final ConcurrentHashMap<Long, Binding> idBindingCache = new ConcurrentHashMap<>();

  // custom build record to handle link of template and TTL
  private record Binding(String template, Instant expiresAt) {
    boolean alive() {
      return Instant.now().isBefore(expiresAt);
    }
  }

  // method to call from conrtoller to link entries
  public void bind(long id, String template, Duration ttl) {
    idBindingCache.put(
        id,
        new Binding(template, Instant.now().plus(ttl)));
  }

  // used only in ResponseRouter
  // tries to get linkage from cahce (here)
  // if FOUND:
  public Optional<String> activeTemplateFor(long id) {
    Binding templateBinding = idBindingCache.get(id);
    // if not found - return empty (so on upper level it will be converted to
    // default tempalate request)
    if (templateBinding == null)
      return Optional.empty();
    // if fount but not alive - so timing is over - remove this entry from cache
    // (self-cleaning operation - atomized, the leave binding free for next reuse)
    // and return empty at the end
    if (!templateBinding.alive()) {
      idBindingCache.remove(id, templateBinding);
      return Optional.empty();
    }
    // if we pass checks (so there is some binding and it's not rotten by time)
    // return this binded template
    return Optional.of(templateBinding.template());

  }

  @Scheduled(cron = AnnotationsParametersConfig.CRON_SWIPE_BINDINGS)
  public void sweepExpired() {
    MDC.put(TRACE_ID, UUID.randomUUID().toString().replace("-", ""));
    try {
      int before = idBindingCache.size();
      idBindingCache.entrySet().removeIf(e -> Instant.now().isAfter(e.getValue().expiresAt()));
      int removed = before - idBindingCache.size();
      if (removed > 0) {
        log.info("Swept {} expired bindings, {} remaining", removed, idBindingCache.size());
      } else {
        log.info("Nothing to delete");
      }
    } catch (Exception e) {
      log.error("Auto-swipe failed", e);
    } finally {
      MDC.remove(TRACE_ID); // ← always clean up, scheduler threads are reused
    }
  }
}
