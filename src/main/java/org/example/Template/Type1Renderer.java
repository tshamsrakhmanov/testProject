package org.example.Template;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class Type1Renderer implements TemplateRenderer {

  @Override
  public String templateName() {
    return "type1";
  }

  @Override
  public void render(ObjectNode out, JsonNode request) {
    out.put("email", "type1@mail.ru");
  }

}
