package com.springboot.blog_boot.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

public class Crypto {
    private static final String algorithm = "AES/CBC/PKCS5Padding";    // 암호화 알고리즘, 패딩
    private static final String key = "58392176596138482241443582018901";
    private static final String iv = key.substring(0, 16);

    public String encrypt(String plaintext) throws GeneralSecurityException {
        // 암호화
        if (plaintext == null) {
            return null;
        }

        Cipher cipher = Cipher.getInstance(algorithm);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec paramSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);

        byte[] cipherBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(cipherBytes);
    }

    public String decrypt(String ciphertext) throws GeneralSecurityException {
        // 복호화
        if (ciphertext == null) {
            return null;
        }

        Cipher cipher = Cipher.getInstance(algorithm);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec paramSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);

        byte[] plainBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));

        return new String(plainBytes, StandardCharsets.UTF_8);
    }
}
