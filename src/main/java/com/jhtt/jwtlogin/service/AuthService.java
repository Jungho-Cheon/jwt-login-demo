package com.jhtt.jwtlogin.service;

import com.jhtt.jwtlogin.Entity.TestUser;
import com.jhtt.jwtlogin.exception.EmailNotFoundException;
import com.jhtt.jwtlogin.exception.PasswordNotMatchException;
import com.jhtt.jwtlogin.exception.RefreshTokenNotFoundException;
import com.jhtt.jwtlogin.payload.LoginRequest;
import com.jhtt.jwtlogin.payload.SignUpRequest;
import com.jhtt.jwtlogin.repository.TestUserRepository;
import com.jhtt.jwtlogin.security.TestUserDetail;
import com.jhtt.jwtlogin.security.jwt.JwtUtils;
import com.jhtt.jwtlogin.security.jwt.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final TestUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public Long signup(SignUpRequest request) {
        String password = request.getPassword();
        request.setPassword(passwordEncoder.encode(password));
        TestUser savedUser = userRepository.save(TestUser.of(request));
        return savedUser.getUserId();
    }

    public Map<String, String> signin(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        TestUser user = userRepository.findTestUserByEmail(email).orElseThrow(() -> new EmailNotFoundException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordNotMatchException("패스워드가 일치하지 않습니다.");
        }

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtUtils.createToken(email, TokenType.access));
        tokens.put("refreshToken", jwtUtils.createToken(email, TokenType.refresh));
        return tokens;
    }

    // username -> email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        TestUser user = userRepository.findTestUserByEmail(email).orElseThrow(() -> new EmailNotFoundException("존재하지 않는 회원입니다."));
        return new TestUserDetail(user.getEmail(), user.getPassword(), "USER");
    }

    public Map<String, String> refreshAllToken(String refreshToken) {
        if (!jwtUtils.isValid(refreshToken, TokenType.refresh)){
            throw new RefreshTokenNotFoundException("올바른 refreshToken이 아닙니다.");
        }
        String email = jwtUtils.getAuthentication(refreshToken, TokenType.refresh);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtUtils.createToken(email, TokenType.access));
        tokens.put("refreshToken", jwtUtils.createToken(email, TokenType.refresh));
        return tokens;
    }

    public boolean checkAccessToken(String accessToken) {
        return jwtUtils.isValid(accessToken, TokenType.access);
    }
}
