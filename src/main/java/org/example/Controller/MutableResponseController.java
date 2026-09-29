package org.example.Controller;

import java.time.Duration;
import java.util.List;

import org.example.Config.ApplicationProperties;
import org.example.DTO.PutCacheDTO;
import org.example.DTO.SetupRequestDTO;
import org.example.Template.BindingRegistry;
import org.example.Template.RendererDispatcher;
import org.example.Template.ResponseRouter;
import org.example.Template.TemplateProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  private final ResponseRouter router;
  private final RendererDispatcher dispatcher;
  private final TemplateProvider templateProvider;
  private final BindingRegistry bindingRegistry;
  private final ApplicationProperties applicationProperties;

  @Operation(summary = "Business handler", description = "Take out response")
  @GetMapping(path = "/result_final")
  public ResponseEntity<?> result(@RequestBody JsonNode request) throws Exception {
    long id = request.get("id").asLong();
    if (request.has("id")) {

      return ResponseEntity.status(HttpStatus.OK).body(dispatcher.render(router.route(id), request));
      // return dispatcher.render(router.route(id), request);
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("no ID provided");
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

  @PutMapping("/result_setup")
  public ResponseEntity<?> setup(@RequestBody SetupRequestDTO requestDTO) {
    if (!templateProvider.exists(requestDTO.getTemplate())) {
      return ResponseEntity.notFound().build();
    }
    bindingRegistry.bind(requestDTO.getId(), requestDTO.getTemplate(),
        Duration.ofSeconds(applicationProperties.ttl_seconds_binding()));
    return ResponseEntity.ok().build();
  }
}
