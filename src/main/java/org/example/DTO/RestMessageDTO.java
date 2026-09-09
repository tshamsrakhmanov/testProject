package org.example.DTO;

import java.util.Map;

import lombok.Data;

@Data
public class RestMessageDTO {

  private String topic;
  private String messageKey;
  private String messageBody;
  private Map<String, String> messageHeaders;

}
