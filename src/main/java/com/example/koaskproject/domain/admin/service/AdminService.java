package com.example.koaskproject.domain.admin.service;


import com.example.koaskproject.domain.admin.Exception.AdminException;
import com.example.koaskproject.domain.admin.dto.AdminLoginRequest;
import com.example.koaskproject.domain.admin.entity.Admin;
import com.example.koaskproject.domain.admin.repository.AdminRepository;
import com.example.koaskproject.global.component.AesUtil;
import com.example.koaskproject.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AdminRepository adminRepository;
    private final AesUtil aesUtil;
    public Admin login(@Valid AdminLoginRequest request) throws Exception {

        Admin admin = adminRepository.findByEmail(request.email())
                .orElseThrow(() -> new AdminException(ErrorCode.DATA_NOT_FOUND,"Email이 "+request.email()+"인 어드민 계정이 존재 하지 않습니다."));

        if(admin.isPasswordMatch(aesUtil.encrypt(request.password())))
            return admin;
        else
            throw new AdminException(ErrorCode.INVALID_PASSWORD);
    }

}