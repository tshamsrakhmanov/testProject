package org.example.Controller;

import org.example.HttpClient.CustomHttpClient;
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
@Tag(name = "OUTCOME side of proxyAPI", description = "")
@RequestMapping(path = "proxyAPI")
public class ProxyController {

  private final CustomHttpClient customHttpClient;

  @Operation(summary = "Generate UUID", description = "uuid output without dashes")
  @GetMapping(path = "/uuid")
  public String requestUUID() {
    return customHttpClient.fetchData();
  }
}
