package com.jhtt.jwtlogin.security.jwt;

import com.jhtt.jwtlogin.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
//        List<Cookie> cookies = Arrays.asList(request.getCookies());
//        cookies.stream().filter(cookie -> cookie.get)
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && jwtUtils.isValid(accessToken, TokenType.access)) {
            String email = jwtUtils.getAuthentication(accessToken, TokenType.access);
            UserDetails userDetails = authService.loadUserByUsername(email);
            SecurityContextHolder.getContext()
                    .setAuthentication(
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                    );
        }

        filterChain.doFilter(request, response);
    }
}
