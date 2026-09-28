package org.example.Template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TemplateProvider {

  public static final String DEFAULT = "default";

  private final Map<String, JsonNode> templateContainer;

  // fill up cache with JSON files at start up of this bean - autorun-like
  // variation
  public TemplateProvider(ObjectMapper om,
      @Value("classpath*:/templates/*.json") Resource[] files) throws IOException {
    Map<String, JsonNode> tmp = new HashMap<>();
    for (Resource r : files) {
      int dotPlace = r.getFilename().indexOf('.');
      String nameClean = r.getFilename();
      String name = nameClean.substring(0, dotPlace);
      JsonNode payload = om.readTree(r.getInputStream());
      if (name == null || !name.matches("[a-z0-9_-]+"))
        throw new IllegalStateException("Bad name in " + r.getFilename());
      if (payload == null || payload.isNull())
        throw new IllegalStateException("Missing payload in " + r.getFilename());
      if (tmp.putIfAbsent(name, payload) != null)
        throw new IllegalStateException("Duplicate template: " + name);
    }

    this.templateContainer = tmp;

  }

  // return cached json
  public JsonNode get(String name) throws Exception {
    log.info("request for template: {}", name);
    JsonNode p = templateContainer.get(name);
    if (p == null)
      throw new Exception("Unknown template: " + name);
    return p;
  }

  // list of all names in cache
  public List<String> getAllTemplates() {
    List<String> res = new ArrayList<>();
    for (String s : templateContainer.keySet()) {
      res.add(s);

    }
    return res;
  }

  public boolean exists(String name) {
    return templateContainer.containsKey(name);
  }

  public Set<String> names() {
    return templateContainer.keySet();
  }
}
