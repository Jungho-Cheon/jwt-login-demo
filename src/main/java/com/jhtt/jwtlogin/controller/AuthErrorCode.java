package com.jhtt.jwtlogin.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode {
    EMAIL_NOT_FOUND(1),
    PASSWORD_NOT_MATCHED(2),
    REFRESH_TOKEN_INVALID(3);

    private final int error;

}
