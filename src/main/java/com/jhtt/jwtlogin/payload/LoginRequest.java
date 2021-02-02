package com.jhtt.jwtlogin.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;

    // TODO 자동 로그인 처리
    private Boolean rememberMe;
}
