package com.example.koaskproject.domain.service;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImageUploadTest {

    @Test
    void testBase64ImageUpload() throws IOException {
        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/w8AAgMBg0t4cYwAAAAASUVORK5CYII=";

        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

        File outputFile = new File("test.png");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(decodedBytes);
        }

        assertTrue(outputFile.exists());

        System.out.println("✅ 이미지 저장 완료: " + outputFile.getAbsolutePath());
    }
}