package org.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeysDTO {
  private String privateKey;
  private String publicKey;
  private String xCoordinate;
  private String yCoordinate;
}
