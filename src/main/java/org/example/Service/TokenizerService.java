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
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.example.DTO.KeysDTO;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenizerService {

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
