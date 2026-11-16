package org.example.HttpClient;

import org.example.Config.ApplicationProperties;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomHttpClient {

  private final ObjectMapper MAPPER;

  private final RestClient restClient;
  private final ApplicationProperties props;

  public String fetchData() {
    String url = props.requestPath();
    String traceId = currentTraceId();

    log.info("Outgoing request: method=GET url={} accept={} traceId={}",
        url, MediaType.APPLICATION_JSON, traceId);

    long start = System.nanoTime();
    try {
      String body = restClient.get()
          .uri(url)
          .accept(MediaType.APPLICATION_JSON)
          .header(props.xTraceIdString(), traceId) // propagate trace id downstream
          .retrieve()
          .body(String.class);

      long ms = (System.nanoTime() - start) / 1_000_000;
      log.info("Incoming response: url={} status=2xx took={}ms traceId={} body={}",
          url, ms, traceId, body);

      return extractResult(body);
    } catch (Exception e) {
      long ms = (System.nanoTime() - start) / 1_000_000;
      log.error("Outgoing request failed: url={} took={}ms traceId={} error={}",
          url, ms, traceId, e.getMessage(), e);
      throw e;
    }
  }

  private String currentTraceId() {
    String traceId = MDC.get(props.traceIdString());
    if (traceId == null || traceId.isBlank()) {
      traceId = java.util.UUID.randomUUID().toString().replace("-", "");
    }
    return traceId;
  }

  private String extractResult(String body) {
    try {
      var node = MAPPER.readTree(body);
      String result = node.path("uuid").asText(null);
      if (result == null || result.isBlank()) {
        throw new IllegalStateException("result field in response null or blank");
      }
      return result;
    } catch (Exception e) {
      log.error("Exception while extract result: {}", e.getMessage(), e);
      throw new IllegalStateException("Failed to parse response", e);
    }
  }

}
