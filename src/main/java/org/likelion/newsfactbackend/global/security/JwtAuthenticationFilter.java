package org.likelion.newsfactbackend.global.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.global.config.RedisConfig;
import org.likelion.newsfactbackend.global.domain.enums.ResultCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisConfig redisConfig;

    private final List<String> passUrl = List.of(
            "/chat/**",
            "/chatting/**",
            "/", // react
            "/api/v1/auth/google/callback",
            "/api/v1/auth/kakao/callback",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/css/**",
            "/images/**",
            "/js/**",
            "/favicon.ico",
            "/ask/**"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        log.info("[JwtAuthenticationFilter] should not filter url : {}", request.getServletPath());
        return passUrl.stream().anyMatch(url -> new AntPathMatcher().match(url, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[jwtauthentication filter] : {}", request.toString());
        String token = jwtTokenProvider.extractToken(request);

        if (!token.isEmpty() && jwtTokenProvider.validateToken(token)) {
            String isLogout = (String) redisConfig.redisTemplate().opsForValue().get(token); // redis storage {token : logout} 있는지 확인
            log.info("[JwtAuthentication Filter] redis key : {}, value : {}", token, isLogout);

            if (!ObjectUtils.isEmpty(isLogout)) { // 로그아웃 된 토큰일 시
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 반환
                response.getWriter().write("This token has been logged out.");
                return;
            } else { // 유효한 토큰 일 시
                try {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception e) {
                    request.setAttribute("error", ResultCode.FAIL);
                }
            }

            filterChain.doFilter(request, response);
        }
    }
}
