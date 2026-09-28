package org.example.Template;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class RendererDispatcher {

  private final Map<String, TemplateRenderer> renderers;

  // init: run through ALL renderers in memory - interface'd classes
  // afte - make a list of them: name - renderer itself
  // IMPORTANT !!!
  // renderers must be declared as @Component
  public RendererDispatcher(List<TemplateRenderer> list) {
    Map<String, TemplateRenderer> templateRenderersMap = new HashMap<>();
    // run through all and alert on Duplicates - halts app
    for (TemplateRenderer r : list) {
      String name = r.templateName();
      if (templateRenderersMap.containsKey(name)) {
        throw new IllegalStateException("Duplicate renderer for: " + name);
      }
      templateRenderersMap.put(name, r);
    }
    this.renderers = Map.copyOf(templateRenderersMap);
    log.info("Registered {} renderers: {}", renderers.size(), renderers.keySet());
  }

  public JsonNode render(Routed routed) {
    ObjectNode out = routed.payload().deepCopy();

    TemplateRenderer r = renderers.get(routed.templateName());
    if (r == null) {
      log.warn("No renderer for template '{}' — returning payload unchanged", routed.templateName());
      return out;
    }

    r.render(out);
    return out;
  }

  public Set<String> names() {
    return renderers.keySet();
  }
}
