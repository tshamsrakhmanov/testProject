package org.example.DTO;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenSigningDTO {
  private Map<String, Object> tokenBody;
  private Map<String, Object> tokenHeaders;
}
