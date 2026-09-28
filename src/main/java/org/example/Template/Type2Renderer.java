package org.example.Template;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class Type2Renderer implements TemplateRenderer {

  @Override
  public String templateName() {
    return "type2";
  }

  @Override
  public void render(ObjectNode out, JsonNode request) {
    out.put("email", request.path("email").asText("type2_default_renderer@mail.ru"));
  }

}
