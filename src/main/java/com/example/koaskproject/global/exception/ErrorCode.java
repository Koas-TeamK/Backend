package com.example.koaskproject.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode
{
    DATA_NOT_FOUND("DATA_NOT_FOUND", "데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_TOKEN("INVALID_TOKEN", "토큰이 유효하지 않습니다..", HttpStatus.BAD_REQUEST),
    EXCEEDS_CAPACITY("EXCEEDS_CAPACITY", "회의실 가용 인원을 초과했습니다.", HttpStatus.BAD_REQUEST),
    TIME_CONFLICT("RESERVATION_CONFLICT", "이미 예약된 시간과 겹칩니다.",HttpStatus.CONFLICT),
    SAVE_FAILED("SAVE_FAILED", "저장이 되지 않았습니다. 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR),
    DEC_FAILED("DEC_FAILED", "복호화가 실패 되었습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    CANCEL_UNAUTHORIZED(
            "CANCEL_UNAUTHORIZED",
            "예약자 명과 달라 예약취소가 불가합니다.",
            HttpStatus.FORBIDDEN
    ),
    ALREADY_REGISTERED(
            "ALREADY_REGISTERED",
            "이미 등록된 회원입니다.",
            HttpStatus.CONFLICT
    );
    private final String code;
    private final String message;
    private final HttpStatus status;
}
