package org.example.Aspects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class RequestLoggingAspect {

  private static final Logger log = LoggerFactory.getLogger(RequestLoggingAspect.class);

  @Around("@within(org.springframework.web.bind.annotation.RestController)")
  public Object logRequest(ProceedingJoinPoint pjp) throws Throwable {
    ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    if (attrs != null) {
      HttpServletRequest req = attrs.getRequest();

      log.info("---");
      log.info("HTTP request");
      log.info("Method:{}, Path: {}", req.getMethod(), req.getRequestURI());

      for (Object arg : pjp.getArgs()) {
        if (arg != null
            && !(arg instanceof HttpServletRequest)
            && !(arg instanceof HttpServletResponse)) {
          log.info("Request mapped: {}", arg);
        }
      }
      log.info("---");
    }

    return pjp.proceed();
  }
}
