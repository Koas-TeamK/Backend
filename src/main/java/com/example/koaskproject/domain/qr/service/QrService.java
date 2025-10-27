package com.example.koaskproject.domain.qr.service;

import com.example.koaskproject.domain.qr.domain.Qr;
import com.example.koaskproject.domain.qr.dto.*;
import com.example.koaskproject.domain.qr.repository.QrRepository;
import com.example.koaskproject.global.component.AesUtil;
import com.example.koaskproject.global.exception.ErrorCode;
import com.example.koaskproject.global.exception.GlobalException;
import com.example.koaskproject.domain.item.entity.Item;
import com.example.koaskproject.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QrService
{
    public final AesUtil aesUtil;
    public final QrRepository qrRepository;
    private static final int PAGE_SIZE = 100;

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
        Qr qr = qrRepository.findBySerialAndItemName(serial,name).orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));

        return CheckQrResponseDto.from(qr);
    }


    public List<QrResponseDto> getAllQrs(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("serial").ascending());
        Page<Qr> qrPage = qrRepository.findAll(pageable);

        return qrPage.stream()
                .map(QrResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public QrResponseDto createQr(QrRequestDto qrCreateDto){

        if(qrRepository.existsBySerialAndItemName(qrCreateDto.serial(), qrCreateDto.itemName()))
            throw new GlobalException(ErrorCode.DUPLICATE_SERIAL);
        Qr saved=qrRepository.save(QrRequestDto.of(qrCreateDto));
        return QrResponseDto.from(saved);
    }


    @Transactional
    public QrResponseDto updateQr(Long id, QrRequestDto request) {
        //이미지는 여기서 변환이 되어야 함
        int updatedCount = qrRepository.updateQrById(
                id,
                request.qrUrl(),
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
                request.startDate(),
                request.endDate(),
                request.itemName(),
                request.serial()
        );

        return qrList.stream()
                .map(QrResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<QrResponseDto> searchQrBySerial(QrSearchBySerialRequest request)
    {
        List<Qr> qrList = qrRepository.findBySerialBetween(request.startSerial(), request.endSerial());

        return qrList.stream()
                .map(QrResponseDto::from)
                .collect(Collectors.toList());
    }
}
