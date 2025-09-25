package com.example.koaskproject.domain.qr.repository;

import com.example.koaskproject.domain.qr.domain.Qr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QrRepository extends JpaRepository<Qr, Long>
{
    Optional<Qr> findBySerial(String serial );
    boolean existsBySerialAndItemName(String serial, String itemName);


    @Modifying
    @Query("UPDATE Qr q SET q.image_url = :imageUrl, q.serial = :serial, q.message = :message, q.createdDate = :createdDate, q.itemName = :itemName WHERE q.id = :id")
    int updateQrById(@Param("id") Long id,
                     @Param("imageUrl") String imageUrl,
                     @Param("serial") String serial,
                     @Param("message") String message,
                     @Param("createdDate") String createdDate,
                     @Param("itemName") String itemName);

    @Query("SELECT q FROM Qr q " +
            "WHERE (:createdDate IS NULL OR q.createdDate = :createdDate) " +
            "AND (:itemName IS NULL OR q.itemName = :itemName) " +
            "AND (:serial IS NULL OR q.serial = :serial)")
    List<Qr> searchQr(
            @Param("createdDate") String createdDate,
            @Param("itemName") String itemName,
            @Param("serial") String serial
    );
}
