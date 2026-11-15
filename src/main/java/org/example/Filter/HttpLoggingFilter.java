package org.example.Filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpLoggingFilter extends OncePerRequestFilter {

  private static final int MAX_LEN_BODY = 1_000;

  private static final List<String> EXCL_PREFIXES = List.of(
      "/actuator",
      "/favicon.ico",
      "/error");

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return EXCL_PREFIXES.stream().anyMatch(path::startsWith);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);

    long start = System.currentTimeMillis();

    log.debug("| HTTP IN <-----");
    log.debug("| URI: {}", req.getRequestURI());
    log.debug("| Method: {}", req.getMethod());
    log.debug("| Headers: {}", formatHeaders(req));

    try {
      filterChain.doFilter(req, res);
    } catch (Exception ex) {
      log.error("| EXCEPTION in: {}", ex.getMessage(), ex);
      throw ex;
    } finally {
      long took = System.currentTimeMillis() - start;

      log.debug("| HTTP OUT ----->");
      log.debug("| Status: {} (took {} ms)", res.getStatus(), took);
      log.debug("| Body IN: {}", truncate(new String(req.getContentAsByteArray(), StandardCharsets.UTF_8)));
      log.debug("| Body OUT: {}", truncate(new String(res.getContentAsByteArray(), StandardCharsets.UTF_8)));

      res.copyBodyToResponse();
    }

  }

  private String formatHeaders(HttpServletRequest req) {
    StringBuilder sb = new StringBuilder();
    for (String name : Collections.list(req.getHeaderNames())) {
      for (String value : Collections.list(req.getHeaders(name))) {
        sb.append(name).append("=").append(value).append("; ");
      }
    }
    return sb.toString();
  }

  private String truncate(String s) {
    if (s == null) {
      return "<null>";
    }
    if (s.isEmpty()) {
      return "<empty>";
    }
    String oneLine = s.replace("\n", "").replace("\r", "");
    return oneLine.length() <= MAX_LEN_BODY
        ? oneLine
        : oneLine.substring(0, MAX_LEN_BODY) + " ... [truncated]";
  }

}
