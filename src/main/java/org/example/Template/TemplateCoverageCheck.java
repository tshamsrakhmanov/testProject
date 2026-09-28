package org.example.Template;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TemplateCoverageCheck {

  // INIT: get all beans of templates and renderers
  // and check if they have 1-to-1 comparison:
  // so for each template there is renderer
  // -> connection goes by names
  // so name of file without .json must be in renderer name String method
  // IMPORTANT !!!
  // renderers must be declared as @Component
  public TemplateCoverageCheck(TemplateProvider templates, RendererDispatcher dispatcher) {
    Set<String> templateNames = templates.names();
    Set<String> rendererNames = dispatcher.names();

    Set<String> missingRenderers = new HashSet<>(templateNames);
    missingRenderers.removeAll(rendererNames);
    if (!missingRenderers.isEmpty()) {
      throw new IllegalStateException("Templates without renderer: " + missingRenderers);
    }

    Set<String> orphanRenderers = new HashSet<>(rendererNames);
    orphanRenderers.removeAll(templateNames);
    if (!orphanRenderers.isEmpty()) {
      throw new IllegalStateException("Renderers without template: " + orphanRenderers);
    }
  }
}
