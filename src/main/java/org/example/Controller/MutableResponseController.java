package org.example.Controller;

import java.util.List;

import org.example.DTO.PutCacheDTO;
import org.example.Template.TemplateProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Changable behaviour conrtoller", description = "The only business conrtoller gives out either default or pre-defined JSON with mutation")
@RequestMapping(path = "mutable_api")
public class MutableResponseController {

  private final TemplateProvider templateProvider;

  @Operation(summary = "Business handler", description = "Take out response")
  @GetMapping(path = "/result")
  public void result() {

  }

  @Operation(summary = "All templates loaded", description = "List of all pre-defined responses")
  @GetMapping(path = "/allTemplates")
  public List<String> allTemplates() {
    return templateProvider.getAllTemplates();

  }

  @Operation(summary = "All templates loaded", description = "List of all pre-defined responses")
  @GetMapping(path = "/exactTemplate")
  public JsonNode exactTemplate(@RequestBody PutCacheDTO requestDTO) throws Exception {
    return templateProvider.get(requestDTO.getValue());

  }
}
