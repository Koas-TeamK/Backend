package com.example.koaskproject.domain.qr.service;

import com.example.koaskproject.domain.qr.domain.Qr;
import com.example.koaskproject.domain.qr.dto.CheckQrResponseDto;
import com.example.koaskproject.domain.qr.repository.QrRepository;
import com.example.koaskproject.global.component.AesUtil;
import com.example.koaskproject.global.exception.ErrorCode;
import com.example.koaskproject.global.exception.GlobalException;
import com.example.koaskproject.domain.item.dto.ItemResponseDto;
import com.example.koaskproject.domain.item.entity.Item;
import com.example.koaskproject.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QrService
{
    public final ItemRepository itemRepository;
    public final AesUtil aesUtil;
    public final QrRepository qrRepository;

    public CheckQrResponseDto checkQr(String name ,String serial, String token)
    {
        try{
            String decrypted = aesUtil.decrypt(token);
            if (!decrypted.equals(serial)) {
                throw new GlobalException(ErrorCode.INVALID_TOKEN);
            }
        } catch (Exception e){
            throw new GlobalException(ErrorCode.DEC_FAILED);
        }
        Qr qr = qrRepository.findBySerial(serial).orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));
        Item item = itemRepository.findByLimitedNumberAndItemType_Name(serial, name)
                .orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));

        return CheckQrResponseDto.from(qr,item);
    }
}
