package com.jhtt.jwtlogin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jhtt.jwtlogin.exception.RefreshTokenNotFoundException;
import com.jhtt.jwtlogin.payload.LoginRequest;
import com.jhtt.jwtlogin.payload.SignUpRequest;
import com.jhtt.jwtlogin.payload.TestRequest;
import com.jhtt.jwtlogin.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000"}) // For React.js Host
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원 가입 엔드 포인트
    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request) throws JsonProcessingException {
        log.info("회원 가입 요청 : {}", request);

        Long id = authService.signup(request);

        Map<String, Object> payload = new HashMap<>();
        payload.put("created-id", id);
        payload.put("message", "회원 가입에 성공하였습니다.");

        return ResponseEntity.ok(payload);
    }

    // 로그인 엔드 포인트
    @PostMapping("/api/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        log.info("로그인 요청 : {}", request);

        Map<String, String> tokens = authService.signin(request);
        String refreshToken = tokens.get("refreshToken");
        tokens.remove("refreshToken");

        Cookie cookie = new Cookie("Refresh-Token", refreshToken);
        cookie.setMaxAge(60 * 60 * 24 * 31); // 31일
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(tokens);
    }

    @PostMapping("/api/refresh-token")
    public ResponseEntity<?> refreshAllToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("refresh-token!");
        List<Cookie> cookies = Arrays.asList(request.getCookies());
        Cookie refreshTokenCookie = cookies.stream()
                .filter(cookie -> cookie.getName().equals("Refresh-Token"))
                .findFirst()
                .orElseThrow(() -> new RefreshTokenNotFoundException("refreshToken을 찾을 수 없습니다."));
        Map<String, String> tokens = authService.refreshAllToken(refreshTokenCookie.getValue());
        String refreshToken = tokens.get("refreshToken");
        tokens.remove("refreshToken");

        Cookie cookie = new Cookie("Refresh-Token", refreshToken);
        cookie.setMaxAge(60 * 60 * 24 * 31); // 31일
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(tokens);
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("Refresh-Token", null);
        cookie.setHttpOnly(true);

        Map<String, String> payload = new HashMap<>();
        payload.put("message", "로그아웃에 성공하였습니다.");

        response.addCookie(cookie);
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/api/test-token")
    public ResponseEntity<?> testToken(@Valid @RequestBody TestRequest request){
        return ResponseEntity.ok(authService.checkAccessToken(request.getAccessToken()));
    }
}
