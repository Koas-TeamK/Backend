package com.example.koaskproject.domain.qr.dto;


public record QrSearchBySerialRequest(
        String startSerial,
        String endSerial
) {
    public QrSearchBySerialRequest {
        // null 검사
        if (startSerial == null || endSerial == null) {
            throw new IllegalArgumentException("startSerial과 endSerial은 null일 수 없습니다.");
        }
        try {
            long start = Long.parseLong(startSerial);
            long end = Long.parseLong(endSerial);
            if (start > end) {
                throw new IllegalArgumentException("startSerial은 endSerial보다 작거나 같아야 합니다.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("startSerial과 endSerial은 숫자 형태여야 합니다.");
        }
    }
}
