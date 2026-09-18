package org.example.Controller;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.example.DTO.KeysDTO;
import org.example.DTO.TokenSigningDTO;
import org.example.Service.TokenizerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Token preparation", description = "Handler to facilitate token generation + keys for them")
@RequestMapping(path = "tokensApi")
public class TokenizerConrtoller {

  private final TokenizerService tokenizerService;
  private final ObjectMapper om;

  @Operation(summary = "Generate keys", description = "Generate 4 keys: public, private, x, y")
  @GetMapping(path = "/keysPack")
  public KeysDTO generateKeys()
      throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException,
      JsonProcessingException {

    KeysDTO keysDTO = tokenizerService.generateEs256Keys();
    log.info("Return: {}", om.writeValueAsString(keysDTO));
    return tokenizerService.generateEs256Keys();

  }

  @Operation(summary = "Sing token", description = "Generate token with sign")
  @PostMapping(path = "/sign")
  public ResponseEntity<String> generateTokenWithSign(@RequestBody TokenSigningDTO tokenSigningDTO)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    log.info("Mapped: {}", tokenSigningDTO);

    String res = tokenizerService.signToken(tokenSigningDTO);
    log.info("Return: {}", res);

    return ResponseEntity.status(HttpStatus.OK).body(res);

  }
}
