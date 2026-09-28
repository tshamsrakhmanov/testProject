package org.example.Template;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class DefaultRenderer implements TemplateRenderer {

  @Override
  public String templateName() {
    return "default";
  }

  @Override
  public void render(ObjectNode out, JsonNode request) {
    out.put("email", "default@mail.ru");
  }

}
