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

  // used only in ResponseRouter
  // tries to get linkage from cahce (here)
  // if FOUND:
  public Optional<String> activeTemplateFor(long id) {
    Binding templateBinding = map.get(id);
    // if not found - return empty (so on upper level it will be converted to
    // default tempalate request)
    if (templateBinding == null)
      return Optional.empty();
    // if fount but not alive - so timing is over - remove this entry from cache
    // (self-cleaning operation)
    // and return empty at the end
    // WARN: is it even safe to clean-up like this?...
    // WARN: so cache will buildup with time - NO SAFE !!!
    if (!templateBinding.alive()) {
      map.remove(id, templateBinding);
      return Optional.empty();
    }
    // if we pass checks (so there is some binding and it's not rotten by time)
    // return this binded template
    return Optional.of(templateBinding.template());

  }
}
