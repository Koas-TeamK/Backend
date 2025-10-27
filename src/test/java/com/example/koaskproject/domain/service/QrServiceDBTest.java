package com.example.koaskproject.domain.service;

import com.example.koaskproject.domain.qr.domain.Qr;
import com.example.koaskproject.domain.qr.dto.QrResponseDto;
import com.example.koaskproject.domain.qr.dto.QrSearchBySerialRequest;
import com.example.koaskproject.domain.qr.repository.QrRepository;
import com.example.koaskproject.domain.qr.service.QrService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class QrServiceDBTest {
    @Autowired
    private QrService qrService;

    @Autowired
    private QrRepository qrRepository;

    @Test
    @DisplayName("QR 시리얼넘버로 불러오기")
    @Transactional
    void getQRBySerial() {
        Qr qrSample = Qr.builder().imageUrl("sds").quUrl("sdsd").serial("0123").build();
        Qr qrSample1 = Qr.builder().imageUrl("sds").quUrl("sdsd").serial("0125").build();
        Qr qrSample2 =  Qr.builder().imageUrl("sds").quUrl("sdsd").serial("1123").build();

        qrRepository.save(qrSample);
        qrRepository.save(qrSample1);
        qrRepository.save(qrSample2);

        List<QrResponseDto> qrResponseDtos = qrService.searchQrBySerial(new QrSearchBySerialRequest("0123","0125"));

        Assertions.assertEquals(2, qrResponseDtos.size());
    }
}
