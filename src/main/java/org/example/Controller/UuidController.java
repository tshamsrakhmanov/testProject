package org.example.Controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "INCOME side of proxy API", description = "")
@RequestMapping(path = "uuidApi")
public class UuidController {

  @Operation(summary = "Generate UUID", description = "uuid output without dashes")
  @GetMapping(path = "/uuid", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> generateUuid() {
    return ResponseEntity.ok(Map.of("uuid", UUID.randomUUID().toString()));
  }

}
