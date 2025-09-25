package com.example.koaskproject.domain.qr.service;

import com.example.koaskproject.domain.qr.domain.Qr;
import com.example.koaskproject.domain.qr.dto.CheckQrResponseDto;
import com.example.koaskproject.domain.qr.dto.QrRequestDto;
import com.example.koaskproject.domain.qr.dto.QrResponseDto;
import com.example.koaskproject.domain.qr.dto.QrSearchRequest;
import com.example.koaskproject.domain.qr.repository.QrRepository;
import com.example.koaskproject.global.component.AesUtil;
import com.example.koaskproject.global.exception.ErrorCode;
import com.example.koaskproject.global.exception.GlobalException;
import com.example.koaskproject.domain.item.entity.Item;
import com.example.koaskproject.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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


    public List<QrResponseDto> getAllQrs() {
        return qrRepository.findAll()
                .stream()
                .map(QrResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public QrResponseDto createQr(QrRequestDto qrCreateDto) {
        if(qrRepository.existsBySerialAndItemName(qrCreateDto.serial(), qrCreateDto.itemName()))
            throw new GlobalException(ErrorCode.DUPLICATE_SERIAL);
        Qr saved=qrRepository.save(QrRequestDto.of(qrCreateDto));
        return QrResponseDto.from(saved);
    }


    @Transactional
    public QrResponseDto updateQr(Long id, QrRequestDto request) {
        int updatedCount = qrRepository.updateQrById(
                id,
                request.imageUrl(),
                request.serial(),
                request.message(),
                request.createdDate(),
                request.itemName()
        );

        if (updatedCount == 0) {
            throw new GlobalException(ErrorCode.DATA_NOT_FOUND);
        }
        Qr updatedQr = qrRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));

        return QrResponseDto.from(updatedQr);
    }

    public List<QrResponseDto> searchQr(QrSearchRequest request) {
        List<Qr> qrList = qrRepository.searchQr(
                request.createdDate(),
                request.itemName(),
                request.serial()
        );

        return qrList.stream()
                .map(QrResponseDto::from)
                .collect(Collectors.toList());
    }
}
