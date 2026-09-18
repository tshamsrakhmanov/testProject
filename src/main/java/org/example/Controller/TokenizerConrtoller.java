package org.example.Controller;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.example.DTO.KeysDTO;
import org.example.Service.TokenizerService;
import org.springframework.web.bind.annotation.GetMapping;
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

  @Operation(summary = "Store value", description = "Put string in cache, with return of entry's UUID - to fetch later")
  @GetMapping(path = "/keysPack")
  public KeysDTO putInCache()
      throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException,
      JsonProcessingException {

    KeysDTO keysDTO = tokenizerService.generateEs256Keys();
    log.info("Return: {}", om.writeValueAsString(keysDTO));
    return tokenizerService.generateEs256Keys();

  }
}
