package org.example.Interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

  private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
    // 1. INCOMING
    log.info("┌───");
    log.info("│ INCOMING");
    log.info("│ Method: {}", req.getMethod());
    log.info("│ Path: {}", req.getRequestURI());
    log.info("│ Query: {}", req.getQueryString());
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest req, HttpServletResponse res,
      Object handler, Exception ex) {
    // 2. OUTGOING — runs AFTER Spring sets the final status
    log.info("│ OUTGOING");
    log.info("│ Status: {}", res.getStatus());
    if (ex != null) {
      log.info("│ Exception: {}", ex.getMessage());
    }
    log.info("└───");
  }
}
