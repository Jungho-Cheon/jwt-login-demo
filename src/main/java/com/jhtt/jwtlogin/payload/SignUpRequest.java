package com.jhtt.jwtlogin.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank
    @Email
    @Length(max = 100)
    private String email;

    @NotBlank
    @Length(min = 8, max = 100)
    private String password;

    private String information;
}
