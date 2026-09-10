package org.example.Aspects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
public class RequestLoggingAspect {

  private static final Logger log = LoggerFactory.getLogger(RequestLoggingAspect.class);

  private final ObjectMapper objectMapper;

  public RequestLoggingAspect(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  private static final int MAX_LOG_LENGTH = 500;

  @Around("@within(org.springframework.web.bind.annotation.RestController)")
  public Object logRequest(ProceedingJoinPoint pjp) throws Throwable {
    ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    if (attrs != null) {
      HttpServletRequest req = attrs.getRequest();

      log.info("┌───");
      log.info("│HTTP request");
      log.info("│Method:{}, Path: {}", req.getMethod(), req.getRequestURI());

      for (Object arg : pjp.getArgs()) {
        if (arg != null
            && !(arg instanceof HttpServletRequest)
            && !(arg instanceof HttpServletResponse)) {
          log.info("│Request mapped: {}", formatForLog(arg));
        }
      }
    }

    Object result;
    try {
      result = pjp.proceed();
    } catch (Throwable t) {
      if (attrs != null) {
        log.info("│---");
        log.info("│HTTP response (exception)");
        log.info("│Exception: {}", t.getMessage());
        log.info("└───");
      }
      throw t;
    }

    if (attrs != null) {
      HttpServletResponse res = attrs.getResponse();

      log.info("│---");
      log.info("│HTTP response");
      if (res != null) {
        log.info("│Status: {}", res.getStatus());
      }
      log.info("│Response: {}", formatForLog(result));
      log.info("└───");
    }

    return result;
  }

  private String formatForLog(Object value) {
    if (value == null) {
      return "null";
    }

    if (value instanceof byte[]
        || value instanceof InputStream
        || value instanceof OutputStream
        || value instanceof Reader
        || value instanceof Writer
        || value instanceof ByteBuffer
        || value instanceof Resource
        || value instanceof MultipartFile) {
      log.warn("│Skipping log for type [{}] - not safe to stringify", value.getClass().getName());
      return "<" + value.getClass().getSimpleName() + " - skipped>";
    }

    // String str = String.valueOf(value);

    String str;
    try {
      str = objectMapper.writeValueAsString(value);
    } catch (Exception e) {
      str = String.valueOf(value);
    }

    if (str.length() > MAX_LOG_LENGTH) {
      return str.substring(0, MAX_LOG_LENGTH) + "... [truncated, total=" + str.length() + " chars]";
    }
    return str;
  }
}
