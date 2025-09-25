package com.example.koaskproject.domain.qr.controller;

import com.example.koaskproject.domain.item.dto.ItemResponseDto;
import com.example.koaskproject.domain.qr.dto.CheckQrResponseDto;
import com.example.koaskproject.domain.qr.service.QrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
public class QrController
{
    private final QrService qrService;
    @GetMapping("/check")
    public ResponseEntity<CheckQrResponseDto> checkMaronQr(
            @RequestParam String name,
            @RequestParam String serial,
            @RequestParam String token
    ){
        return ResponseEntity.ok(qrService.checkQr(name,serial,token));
    }

}
