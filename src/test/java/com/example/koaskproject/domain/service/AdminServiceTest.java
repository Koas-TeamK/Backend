package com.example.koaskproject.domain.service;

import com.example.koaskproject.domain.admin.dto.AdminLoginRequest;
import com.example.koaskproject.domain.admin.entity.Admin;
import com.example.koaskproject.domain.admin.entity.Role;
import com.example.koaskproject.domain.admin.repository.AdminRepository;
import com.example.koaskproject.domain.admin.service.AdminService;
import com.example.koaskproject.domain.qr.service.QrService;
import com.example.koaskproject.global.component.AesUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest
public class AdminServiceTest {
    @Mock
    private AdminRepository adminRepository;

    @Autowired
    private AesUtil aesUtil;
    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminService = new AdminService(adminRepository, aesUtil);
    }
    @Test
    @DisplayName("로그인 성공 - 올바른 이메일과 비밀번호")
    void loginSuccess() throws Exception {
        // given
        String email = "test@admin.com";
        String password = "password123";

        AdminLoginRequest request = new AdminLoginRequest(email, password);

        Admin mockAdmin = Admin.builder().email("test@admin.com").role(Role.ADMIN).password("sedqQiNY_ZFQMOt_nqqv8Q").build();
        Mockito.when(adminRepository.findByEmail(email)).thenReturn(Optional.of(mockAdmin));


        Admin result = adminService.login(request);

        Assertions.assertNotNull(result);
        Mockito.verify(adminRepository).findByEmail(email);
    }
}
