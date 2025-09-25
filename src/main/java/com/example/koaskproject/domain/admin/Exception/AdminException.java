package com.example.koaskproject.domain.admin.Exception;



import com.example.koaskproject.global.exception.ErrorCode;
import com.example.koaskproject.global.exception.GlobalException;


public class AdminException extends GlobalException
{
    public AdminException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AdminException(ErrorCode errorCode) {
        super(errorCode, null);
    }

}
