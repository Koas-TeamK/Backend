package com.example.koaskproject.domain.qr.repository;

import com.example.koaskproject.domain.qr.domain.Qr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface QrRepository extends JpaRepository<Qr, Long>
{
    Optional<Qr> findBySerial(String serial );
    boolean existsBySerialAndItemName(String serial, String itemName);


    @Modifying
    @Query("""
    UPDATE Qr q\s
    SET q.quUrl = :qrUrl,\s
        q.serial = :serial,\s
        q.message = :message,\s
        q.createdDate = :createdDate,\s
        q.itemName = :itemName\s
    WHERE q.id = :id
""")
    int updateQrById(@Param("id") Long id,
                     @Param("qrUrl") String qrUrl,
                     @Param("serial") String serial,
                     @Param("message") String message,
                     @Param("createdDate") String createdDate,
                     @Param("itemName") String itemName);



    @Query("SELECT q FROM Qr q " +
            "WHERE (:startDate IS NULL OR q.createdDate >= :startDate) " +
            "AND (:endDate IS NULL OR q.createdDate <= :endDate) " +
            "AND (:itemName IS NULL OR q.itemName = :itemName) " +
            "AND (:serial IS NULL OR q.serial = :serial) " +
            "ORDER BY q.serial ASC")
    List<Qr> searchQr(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("itemName") String itemName,
            @Param("serial") String serial
    );
}
