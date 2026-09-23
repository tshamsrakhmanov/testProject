package org.example.Security;

public class TokenSigningException extends RuntimeException {

  public TokenSigningException(String message) {
    super(message);
  }

  public TokenSigningException(String message, Throwable cause) {
    super(message, cause);
  }
}
