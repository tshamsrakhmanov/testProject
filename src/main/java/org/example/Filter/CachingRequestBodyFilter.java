package org.example.Filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CachingRequestBodyFilter implements Filter {
  // Filter class to store body of incoming message
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    if (servletRequest instanceof HttpServletRequest httpServletRequest) {
      filterChain.doFilter(new ContentCachingRequestWrapper(httpServletRequest), servletResponse);
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

}
