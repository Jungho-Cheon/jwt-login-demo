package com.jhtt.jwtlogin.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret.access}")
    private String accessSecret;

    @Value("${jwt.secret.refresh}")
    private String refreshSecret;

    private final Long accessTokenExpires = (long) (1000 * 50 * 60); // 1시간
    private final Long refreshTokenExpires = (long) (1000 * 60 * 60 * 24 * 7); // 7일

    public String createToken(String email, TokenType tokenType) {
        Date now = new Date();
        Long expire = tokenType == TokenType.access ? accessTokenExpires : refreshTokenExpires;
        Date expiration = new Date(now.getTime() + expire);
        String secret = TokenType.access == tokenType ? accessSecret : refreshSecret;

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", Arrays.asList("credentials", "roles"));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isValid(String jwt, TokenType tokenType) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(
                    tokenType == TokenType.access ? accessSecret : refreshSecret
            ).parseClaimsJws(jwt);
            return claimsJws.getBody().getExpiration().after(new Date());
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.error("jwt parse exception : {}", e.getMessage());
        }
        return false;
    }

    public String getAuthentication(String token, TokenType tokenType) {
        String secret = tokenType == TokenType.access ? accessSecret : refreshSecret;
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
}
