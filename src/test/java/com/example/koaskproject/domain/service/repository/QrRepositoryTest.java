package com.example.koaskproject.domain.service.repository;

import com.example.koaskproject.domain.qr.domain.Qr;
import com.example.koaskproject.domain.qr.dto.QrRequestDto;
import com.example.koaskproject.domain.qr.dto.QrResponseDto;
import com.example.koaskproject.domain.qr.repository.QrRepository;
import com.example.koaskproject.domain.qr.service.QrService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // H2 사용
class QrRepositoryTest {

    @Autowired
    private QrRepository qrRepository;

    private Qr qr;
    @PersistenceContext
    private EntityManager em;


    private QrRequestDto qrRequestDto;
    @Autowired
    private QrService qrService;
    @BeforeEach
    void setup() {
        qr=Qr.builder().serial("001")
                .createdDate("2025-09-25").message("oldMessage").imageUrl("imageUrl").itemName("sd").build();
        qrRepository.save(qr);

        qrRequestDto = new QrRequestDto(
                "newImage",
                "sdds",
                "002",
                "newMessage",
                "2025-09-26",
                "ItemB"
        );
    }

    @Test
    @DisplayName("QR 서비스 updateQr 테스트")
    void updateQr_Success() {
        // 서비스 호출
        QrResponseDto updated = qrService.updateQr(qr.getId(), qrRequestDto);

        em.clear();
        // DB에서 값 확인
        Qr qrFromDb = qrRepository.findById(qr.getId()).orElseThrow();

        assertEquals("newImage", updated.imageUrl());
        assertEquals("newMessage", updated.message());
        assertEquals("2025-09-26", updated.createdDate());
        assertEquals("ItemB", updated.itemName());

        // DB 값도 확인
        assertEquals("newImage", qrFromDb.getImageUrl());
        assertEquals("002", qrFromDb.getSerial());
        assertEquals("newMessage", qrFromDb.getMessage());
        assertEquals("2025-09-26", qrFromDb.getCreatedDate());
        assertEquals("ItemB", qrFromDb.getItemName());
    }
    @Test
    void updateQrByIdTest() {
        int updatedCount = qrRepository.updateQrById(
                qr.getId(),
                "newImage",
                "002",
                "newMessage",
                "2025-09-26",
                "ItemB"
        );
        assertEquals(1, updatedCount);
        em.flush();
        em.clear();
        Qr updatedQr = qrRepository.findById(qr.getId()).orElseThrow();
        System.out.println(updatedQr.getImageUrl());

        assertEquals("newImage", updatedQr.getImageUrl());
        assertEquals("002", updatedQr.getSerial());
        assertEquals("newMessage", updatedQr.getMessage());
        assertEquals("2025-09-26", updatedQr.getCreatedDate());
        assertEquals("ItemB", updatedQr.getItemName());
    }

    @Test
    @DisplayName("searchQr - 모든 조건 null")
    void searchQr_AllNull() {
        List<Qr> result = qrRepository.searchQr(null, null, null,null);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    @DisplayName("searchQr - createdDate 조건만")
    void searchQr_ByCreatedDate() {
        List<Qr> result = qrRepository.searchQr(null,"2025-09-25", null, null);
        List<Qr> result2 = qrRepository.searchQr(null,"2025-09-24", null, null);
        Assertions.assertEquals(0, result2.size());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("001", result.get(0).getSerial());
    }

}
