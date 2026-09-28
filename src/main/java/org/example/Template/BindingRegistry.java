package org.example.Template;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BindingRegistry {

  // cache to store link of id <-> template <-> TTL
  // id is used as KEY - to be able to have only one id linkage
  private final ConcurrentHashMap<Long, Binding> map = new ConcurrentHashMap<>();

  // custom build record to handle link of template and TTL
  private record Binding(String template, Instant expiresAt) {
    boolean alive() {
      return Instant.now().isBefore(expiresAt);
    }
  }

  // method to call from conrtoller to link entries
  public void bind(long id, String template, Duration ttl) {
    map.put(id, new Binding(template, Instant.now().plus(ttl)));
  }

  // ????
  public Optional<String> activeTemplateFor(long id) {
    Binding b = map.get(id);
    if (b == null)
      return Optional.empty();
    if (!b.alive()) {
      map.remove(id, b);
      return Optional.empty();
    }
    return Optional.of(b.template());
  }
}
