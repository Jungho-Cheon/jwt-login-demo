package com.jhtt.jwtlogin.controller;

import com.jhtt.jwtlogin.exception.EmailNotFoundException;
import com.jhtt.jwtlogin.exception.PasswordNotMatchException;
import com.jhtt.jwtlogin.exception.RefreshTokenNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<?> emailNotFoundExceptionHandler(EmailNotFoundException ex){
        return getSimpleErrorResponse(ex, "존재하지 않는 이메일입니다.", AuthErrorCode.EMAIL_NOT_FOUND);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<?> passwordNotMatchException(PasswordNotMatchException ex){
        return getSimpleErrorResponse(ex, "패스워드가 일치하지 않습니다.", AuthErrorCode.PASSWORD_NOT_MATCHED);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<?> refreshTokenNotFoundException(RefreshTokenNotFoundException ex){
        return getSimpleErrorResponse(ex, "Refresh Token이 올바르지 않습니다.", AuthErrorCode.REFRESH_TOKEN_INVALID);
    }

    private ResponseEntity<?> getSimpleErrorResponse(Exception ex, String message, AuthErrorCode errorCode) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", message);
        payload.put("error-code", errorCode.getError());

        return ResponseEntity.badRequest().body(payload);
    }
}
