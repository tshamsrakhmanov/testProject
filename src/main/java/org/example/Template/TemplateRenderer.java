package org.example.Template;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface TemplateRenderer {

  // name to use in linkage for other components
  String templateName();

  // in-fact renderer that must be used - defined per renderer
  void render(ObjectNode out, JsonNode request);
}
