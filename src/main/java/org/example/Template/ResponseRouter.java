package org.example.Template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseRouter {

  private final BindingRegistry bindings;
  private final TemplateProvider templates;

  // the only method
  // based on given ID of request:
  // takes template to use - based on BindingRegistry
  // if there is no cached behaviour - default (TemplateProvider.DEFAULT)
  // if there IS cached behaviour - get template according to the name
  // either way - gives Route - a linkage of templateName/rendererName and
  // JsonNode to render
  public Routed route(long id) throws Exception {
    String templateName = bindings.activeTemplateFor(id).orElse(TemplateProvider.DEFAULT);
    return new Routed(templateName, templates.get(templateName));
  }
}
