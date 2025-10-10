package com.example.koaskproject.domain.service;


import com.example.koaskproject.domain.qr.domain.Qr;
import com.example.koaskproject.domain.qr.dto.CheckQrResponseDto;
import com.example.koaskproject.domain.qr.dto.QrRequestDto;
import com.example.koaskproject.domain.qr.dto.QrResponseDto;
import com.example.koaskproject.domain.qr.repository.QrRepository;
import com.example.koaskproject.global.component.AesUtil;
import com.example.koaskproject.global.exception.ErrorCode;
import com.example.koaskproject.global.exception.GlobalException;
import com.example.koaskproject.domain.item.entity.Item;
import com.example.koaskproject.domain.item.entity.ItemType;
import com.example.koaskproject.domain.item.repository.ItemRepository;
import com.example.koaskproject.domain.qr.service.QrService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class QrServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private QrRepository qrRepository;

    @Autowired
    private AesUtil aesUtil;
    @InjectMocks
    private QrService qrService;

    private final Qr qrSample = new Qr(1L, "imageUrl", "d","message", "2025-09-25", "ItemA", "123");

    private final QrRequestDto qrRequestDto = new QrRequestDto(
            "sdsddsdsdsdsds",
            "sd",
            "123",
            "message",
            "2025-09-25",
            "ItemA"
    );

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        qrService = new QrService(itemRepository, aesUtil,qrRepository);
    }
    @Test
    void testCheckMaronQr_ValidToken() {
        String itemName = "ClassicChair";
        String serial = "0001";
        String token = "gCXixqZVXB4mqs_XgsADxQ";

        ItemType itemType = ItemType.builder().name(itemName).description("desc").build();
        Item mockItem = Item.builder().itemType(itemType).limitedNumber(serial).build();
        Qr qr= Qr.builder().serial("0001").message("sdsd").createdDate("2025-09-08").build();

        System.out.println(qr.getCreatedDate());
        Mockito.when(itemRepository.findByLimitedNumberAndItemType_Name(serial, itemName))
                .thenReturn(Optional.of(mockItem));
        Mockito.when(qrRepository.findBySerial("0001"))
                .thenReturn(Optional.of(qr));

        CheckQrResponseDto response = qrService.checkQr(itemName, serial, token);

        Assertions.assertEquals(serial, response.serial());
        Assertions.assertEquals(itemName, response.itemName());
    }

    @Test
    void encrypt() throws Exception {
        String encoding = aesUtil.encrypt("password123");
        System.out.println(encoding);
        System.out.println(aesUtil.decrypt(encoding));
    }


    @Test
    void testCheckMaronQr_InvalidToken() throws Exception {
        // given
        String itemName = "ClassicChair";
        String serial = "A001";
        String invalidToken = "invalidToken";

        // when / then
        GlobalException ex = Assertions.assertThrows(GlobalException.class,
                () -> qrService.checkQr(itemName, serial, invalidToken));
        Assertions.assertEquals(ErrorCode.DEC_FAILED, ex.getErrorCode());
    }

    @Test
    void testCheckMaronQr_ItemNotFound() throws Exception {
        // given
        String itemName = "ClassicChair";
        String serial = "1";
        String token = "k8fivga3IATkbsfI9FN5pw==";

        Mockito.when(itemRepository.findByLimitedNumberAndItemType_Name(serial, itemName))
                .thenReturn(Optional.empty());

        // when / then
        GlobalException ex = Assertions.assertThrows(GlobalException.class,
                () -> qrService.checkQr(itemName, serial, token));
        Assertions.assertEquals(ErrorCode.DATA_NOT_FOUND, ex.getErrorCode());
    }


    @Test
    @DisplayName("모든 QR 조회")
    void getAllQrsTest() {
        Mockito.when(qrRepository.findAll()).thenReturn(List.of(qrSample));

        List<QrResponseDto> result = qrService.getAllQrs(0);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(qrSample.getImageUrl(), result.get(0).imageUrl());
    }

    @Test
    @DisplayName("QR 생성 - 성공")
    void createQrSuccess() throws FileNotFoundException {
        Mockito.when(qrRepository.existsBySerialAndItemName(qrRequestDto.serial(), qrRequestDto.itemName()))
                .thenReturn(false);

        Mockito.when(qrRepository.save(Mockito.any(Qr.class))).thenReturn(qrSample);

        QrResponseDto response = qrService.createQr(qrRequestDto);
        System.out.println("생성된 큐알은 "+response);
        Mockito.verify(qrRepository).save(Mockito.any(Qr.class));
    }

    @Test
    @DisplayName("QR 생성 - 중복 시 예외")
    void createQrDuplicate() {
        Mockito.when(qrRepository.existsBySerialAndItemName(qrRequestDto.serial(), qrRequestDto.itemName()))
                .thenReturn(true);

        GlobalException exception = Assertions.assertThrows(GlobalException.class,
                () -> qrService.createQr(qrRequestDto));

        Assertions.assertEquals(ErrorCode.DUPLICATE_SERIAL, exception.getErrorCode());
    }

    // ===================== updateQr =====================
    @Test
    @DisplayName("QR 업데이트 - 성공")
    void updateQrSuccess() {
        Mockito.when(qrRepository.updateQrById(
                Mockito.eq(qrSample.getId()),
                Mockito.eq(qrRequestDto.qrUrl()),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString()
        )).thenReturn(1);

        Mockito.when(qrRepository.findById(qrSample.getId()))
                .thenReturn(Optional.of(qrSample));

        QrResponseDto response = qrService.updateQr(qrSample.getId(), qrRequestDto);
        System.out.println("변경된 큐알은"+response);
        Assertions.assertEquals(qrSample.getId(), response.id());
        Mockito.verify(qrRepository).updateQrById(
                Mockito.eq(qrSample.getId()),
                Mockito.eq(qrRequestDto.qrUrl()),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString()
        );
    }

    @Test
    @DisplayName("QR 업데이트 - 존재하지 않는 ID")
    void updateQrNotFound() {
        Mockito.when(qrRepository.updateQrById(
                Mockito.anyLong(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString()
        )).thenReturn(0);

        GlobalException exception = Assertions.assertThrows(GlobalException.class,
                () -> qrService.updateQr(99L, qrRequestDto));

        Assertions.assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
    }
}