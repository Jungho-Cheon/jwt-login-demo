package com.jhtt.jwtlogin.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class TestRequest {
    @NotBlank
    private String accessToken;
}
