package com.example.koaskproject.domain.qr.repository;

import com.example.koaskproject.domain.qr.domain.Qr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrRepository extends JpaRepository<Qr, Long>
{
    Optional<Qr> findBySerial(String serial );

}
