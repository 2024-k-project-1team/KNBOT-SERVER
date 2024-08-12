package org.likelion.newsfactbackend.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.domain.auth.service.AuthService;
import org.likelion.newsfactbackend.global.domain.enums.LoginType;
import org.likelion.newsfactbackend.global.domain.enums.ResultCode;
import org.likelion.newsfactbackend.global.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    @Operation(summary = "구글 소셜 로그인 콜백 컨트롤러 입니다.")
    @GetMapping("/google/callback")
    public ResponseEntity<?> getGoogleAuthorizeCode(@RequestParam("code") String authorizeCode){
        String type = LoginType.GOOGLE.toString();
        log.info("[google login] issue authorizecode successful");
        log.info("[google login] authorizecode : {}",authorizeCode);
        return authService.signIn(authorizeCode, type);
    }
    @Operation(summary = "카카오 소셜 로그인 콜백 컨트롤러 입니다.")
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> getKaKaoAuthorizeCode(@RequestParam("code") String authorizeCode){
        String type = LoginType.KAKAO.toString();
        log.info("[kakao login] issue authorizecode successful");
        log.info("[kakao login] authorizecode : {}",authorizeCode);
        return authService.signIn(authorizeCode, type);
    }
    @Operation(summary = "사용자 로그인 후 로그아웃 기능입니다.", description = "jwt 만료")
    @DeleteMapping("/log-out")
    public ResponseEntity<?> logOut(HttpServletRequest request){
        String token = jwtTokenProvider.extractToken(request);

        if(token == null){
            log.error("[logout] token is null!");
            return ResultCode.TOKEN_IS_NULL.toResponseEntity();
        }
        return authService.logOut(token);
    }
}
