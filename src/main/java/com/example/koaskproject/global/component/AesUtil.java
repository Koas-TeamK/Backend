package com.example.koaskproject.global.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AesUtil {
    @Value("${aes.secret.key}")
    private String secretKey;
    @Value("${aes.iv}")
    private String iv;
    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted);
    }

    public String decrypt(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decodedBytes = Base64.getUrlDecoder().decode(encrypted);
        byte[] decrypted = cipher.doFinal(decodedBytes);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

}