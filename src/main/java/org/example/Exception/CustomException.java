package org.example.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CustomException extends RuntimeException {

  private final String storageName;
  private final long currentSize;
  private final long maxSize;

  // sampe custom exception from one of the previous projects to use later
  @Override
  public String getMessage() {
    return String.format("Storage '%s' exceeded maximum capacity: %d/%d records", storageName, currentSize, maxSize);

  }

}
