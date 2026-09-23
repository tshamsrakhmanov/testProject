package org.example.Security;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  // Custom class to modify exception handling in restControllers

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {

    if (ex.getCause() instanceof JsonParseException) {
      log.error("JSON syntax error - malformed structure");
    } else if (ex.getCause() instanceof InvalidFormatException) {
      log.error("Value deserialization error - wrong format or invalid value");
    } else {
      log.error("HttpMessageNotReadableException of body deserialization");
    }
    logStdOutput(ex);
    logIncomingMessage(request);

    return new ErrorResponse(
        "HttpMessageNotReadableException ,request body deserialization failed: " + ex.getMessage());

  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

    logStdOutput(ex);
    logIncomingMessage(request);
    return new ErrorResponse("MethodArgumentNotValidException, validation failed: " + ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ErrorResponse handleGeneralExceptin(Exception ex, HttpServletRequest request) {

    log.error("Unexpected error");
    logStdOutput(ex);
    logIncomingMessage(request);
    return new ErrorResponse("Unexpected error: " + ex.getMessage());
  }

  private String getRequestBody(HttpServletRequest request) {
    if (request instanceof ContentCachingRequestWrapper wrapper) {
      byte[] content = wrapper.getContentAsByteArray();
      if (content.length > 0) {
        return new String(content, StandardCharsets.UTF_8);
      }
    }
    try {
      return request.getReader().lines().reduce("", (acc, line) -> acc + line);
    } catch (Exception e) {
      return "[Unable to read body]";
    }
  }

  private void logIncomingMessage(HttpServletRequest request) {
    try {
      String body = getRequestBody(request);
      log.error("Recovered body: {}", body.replace(" ", "").replace("\n", ""));
    } catch (Exception e) {
      log.error("Could not read request body for logging: {}", e.getMessage());
    }
  }

  private void logStdOutput(Exception e) {
    log.error("{}", e.getMessage());
  }

  record ErrorResponse(String errorMessage) {
  }

}
