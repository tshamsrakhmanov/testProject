package org.example.HttpClient;

import org.example.Config.ApplicationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomHttpClient {

  private final RestClient restClient;
  private final ApplicationProperties props;

  public String fetchData() {
    String body = restClient.post()
        .uri(props.requestPath())
        .contentType(MediaType.APPLICATION_JSON)
        .body("{}")
        .retrieve()
        .body(String.class);
    return extractResult(body);
  }

  private String extractResult(String body) {
    try {
      var node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(body);
      String result = node.path("answer").asText(null);
      if (result == null || result.isBlank()) {
        throw new IllegalStateException("result field in response null or blank");
      }
      return result;
    } catch (Exception e) {
      throw new IllegalStateException("Failed to parse response", e);
    }

  }

}
