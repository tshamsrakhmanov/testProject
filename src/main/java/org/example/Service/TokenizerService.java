package org.example.Service;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.example.DTO.KeysDTO;
import org.example.DTO.TokenSigningDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenizerService {

  @Value("${custom.privateKey}")
  private String privateKey;

  public String signToken(TokenSigningDTO tokenSigningDTO) throws NoSuchAlgorithmException, InvalidKeySpecException {

    Map<String, Object> jwtBody = new HashMap<String, Object>(tokenSigningDTO.getTokenBody());

    if (jwtBody.containsKey("iat") || jwtBody.containsKey("IAT") || jwtBody.containsKey("exp")
        || jwtBody.containsKey("EXP")) {
      log.error("iat exp IAT EXP detected, abort");
      throw new RuntimeException("iat exp IAT EXP detected, not allowed");
    }

    JwtBuilder jwt = Jwts.builder();

    for (Map.Entry<String, Object> entry : jwtBody.entrySet()) {
      jwt.claim(entry.getKey(), entry.getValue());
    }

    Instant now = Instant.now();
    long iat = now.getEpochSecond();
    Instant shift = now.plusSeconds(600);
    long exp = shift.getEpochSecond();
    jwt.claim("iat", iat);
    jwt.claim("exp", exp);

    if (tokenSigningDTO.getTokenHeaders() != null) {
      log.info("Headers detected: {}", tokenSigningDTO.getTokenHeaders());
      Map<String, Object> jwtHeaders = new HashMap<>(tokenSigningDTO.getTokenHeaders());
      Map<String, Object> headers = new HashMap<>(jwtHeaders);
      jwt.setHeader(headers);
    }

    byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("EC");
    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

    String jwtTokenSigned = jwt.signWith(privateKey, SignatureAlgorithm.ES256).compact();

    return jwtTokenSigned;

  }

  public KeysDTO generateEs256Keys()
      throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException {
    // p1. basic keys
    KeyPair keyPair = generateKey();
    PrivateKey privateKey = keyPair.getPrivate();
    PublicKey publicKey = keyPair.getPublic();
    byte[] privateKeyBytes = privateKey.getEncoded();
    byte[] publicKeyBytes = publicKey.getEncoded();
    String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKeyBytes);
    String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);
    // p2. x-y coordinates
    // KeyFactory keyFactory = KeyFactory.getInstance("EC");
    KeyFactory keyFactory = KeyFactory.getInstance("EC");
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
    PublicKey publicKey_1 = keyFactory.generatePublic(keySpec);
    ECPoint point = ((ECPublicKey) publicKey_1).getW();
    BigInteger x = point.getAffineX();
    BigInteger y = point.getAffineY();
    String xCoordinateBase64 = Base64.getEncoder().withoutPadding().encodeToString(x.toByteArray());
    String yCoordinateBase64 = Base64.getEncoder().withoutPadding().encodeToString(y.toByteArray());

    return new KeysDTO(
        privateKeyBase64,
        publicKeyBase64,
        xCoordinateBase64,
        yCoordinateBase64);

  }

  public void generateEs256SignedToken() {

  }

  private static KeyPair generateKey() {
    try {
      ECGenParameterSpec spec = new ECGenParameterSpec("secp256r1");
      KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
      generator.initialize(spec, new SecureRandom());
      return generator.generateKeyPair();
    } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
      throw new IllegalStateException("EC/secp256r1 not available", e);
    }
  }

}
