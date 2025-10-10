package com.example.koaskproject.domain.admin.controller;


import com.example.koaskproject.domain.admin.dto.AdminLoginRequest;
import com.example.koaskproject.domain.admin.entity.Admin;
import com.example.koaskproject.domain.admin.service.AdminService;
import com.example.koaskproject.domain.qr.dto.QrRequestDto;
import com.example.koaskproject.domain.qr.dto.QrResponseDto;
import com.example.koaskproject.domain.qr.dto.QrSearchRequest;
import com.example.koaskproject.domain.qr.service.QrService;
import com.example.koaskproject.global.aop.RefreshTokenCheck;
import com.example.koaskproject.global.auth.dto.response.TokenResponse;
import com.example.koaskproject.global.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
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
    ) throws Exception {
        Admin admin=adminService.login(request);

        TokenResponse tokenResponse = authService.login(admin.getId(), admin.getRole().toString());
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/qr")
    @RefreshTokenCheck
    public ResponseEntity<List<QrResponseDto>> getAllQr(
            @RequestParam(defaultValue = "0") int page ) {
        List<QrResponseDto> qrList = qrService.getAllQrs(page);
        return ResponseEntity.ok(qrList);
    }

    @PostMapping("/search")
    @RefreshTokenCheck
    public ResponseEntity<List<QrResponseDto>> searchQr(@RequestBody QrSearchRequest request) {
        System.out.println("🔍 QrSearchRequest: " + request);

        List<QrResponseDto> results = qrService.searchQr(request);
        return ResponseEntity.ok(results);
    }



    @PostMapping("/qr")
    @RefreshTokenCheck
    public ResponseEntity<QrResponseDto> createQr(@RequestBody @Valid QrRequestDto request) throws FileNotFoundException {
        QrResponseDto response = qrService.createQr(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/qr/{id}")
    @RefreshTokenCheck
    public ResponseEntity<QrResponseDto> updateQr(
            @PathVariable Long id,
            @RequestBody @Valid QrRequestDto request) {
        QrResponseDto response = qrService.updateQr(id, request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue()
    {
        return ResponseEntity.ok(authService.reissue());
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout()
    {
        authService.logout();
        return ResponseEntity.noContent().build();
    }

}
