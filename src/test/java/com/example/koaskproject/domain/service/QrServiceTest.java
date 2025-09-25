package com.example.koaskproject.domain.service;


import com.example.koaskproject.domain.qr.domain.Qr;
import com.example.koaskproject.domain.qr.dto.CheckQrResponseDto;
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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
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
        String encoding = aesUtil.encrypt("0001");
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
}