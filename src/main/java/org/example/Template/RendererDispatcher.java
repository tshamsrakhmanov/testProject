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

  private final Map<String, TemplateRenderer> renderersCache;

  // INIT: run through ALL renderers in memory - interface'd classes
  // afte - make a list of them: name - renderer itself
  // IMPORTANT !!!
  // renderers must be declared as @Component
  public RendererDispatcher(List<TemplateRenderer> list) {
    Map<String, TemplateRenderer> templateRenderersMap = new HashMap<>();
    // run through all and alert on Duplicates - halts app
    for (TemplateRenderer foundRenderer : list) {
      String rendererName = foundRenderer.templateName();
      if (templateRenderersMap.containsKey(rendererName)) {
        throw new IllegalStateException("Duplicate renderer for: " + rendererName);
      }
      templateRenderersMap.put(rendererName, foundRenderer);
    }
    this.renderersCache = Map.copyOf(templateRenderersMap);
    log.info("Registered {} renderers: {}", renderersCache.size(), renderersCache.keySet());
  }

  // public method to use after getting a template and further need of render
  // take struct of rendererName-JsonNode and applies given renderer to JsonNode
  public JsonNode render(Routed routed, JsonNode request) {
    ObjectNode out = routed.payload().deepCopy();

    TemplateRenderer renderer = renderersCache.get(routed.templateName());
    if (renderer == null) {
      log.warn("No renderer for template '{}' — returning payload unchanged", routed.templateName());
      return out;
    }

    renderer.render(out, request);
    return out;
  }

  // public method for coverageCheck - in fact just give all Registered names of
  // renderers
  public Set<String> names() {
    return renderersCache.keySet();
  }
}
