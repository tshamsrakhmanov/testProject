package org.example.Template;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface TemplateRenderer {

  String templateName();

  void render(ObjectNode out);
}
