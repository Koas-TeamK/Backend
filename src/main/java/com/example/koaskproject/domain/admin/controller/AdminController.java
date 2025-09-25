package com.example.koaskproject.domain.admin.controller;


import com.example.koaskproject.domain.admin.dto.AdminLoginRequest;
import com.example.koaskproject.domain.admin.entity.Admin;
import com.example.koaskproject.domain.admin.service.AdminService;
import com.example.koaskproject.domain.qr.dto.QrRequestDto;
import com.example.koaskproject.domain.qr.dto.QrResponseDto;
import com.example.koaskproject.domain.qr.dto.QrSearchRequest;
import com.example.koaskproject.domain.qr.service.QrService;
import com.example.koaskproject.global.auth.dto.response.TokenResponse;
import com.example.koaskproject.global.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController
{
    private final AuthService authService;
    private final AdminService adminService;
    private final QrService qrService;
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody @Valid AdminLoginRequest request
    )
    {
        Admin admin=adminService.login(request);

        TokenResponse tokenResponse = authService.login(null, admin.getRole().toString());

        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/qr")
    public ResponseEntity<List<QrResponseDto>> getAllQr() {
        List<QrResponseDto> qrList = qrService.getAllQrs();
        return ResponseEntity.ok(qrList);
    }

    @PostMapping("/search")
    public ResponseEntity<List<QrResponseDto>> searchQr(@RequestBody QrSearchRequest request) {
        List<QrResponseDto> results = qrService.searchQr(request);
        return ResponseEntity.ok(results);
    }



    @PostMapping
    public ResponseEntity<QrResponseDto> createQr(@RequestBody @Valid QrRequestDto request) {
        QrResponseDto response = qrService.createQr(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QrResponseDto> updateQr(
            @PathVariable Long id,
            @RequestBody @Valid QrRequestDto request) {
        QrResponseDto response = qrService.updateQr(id, request);
        return ResponseEntity.ok(response);
    }

}
