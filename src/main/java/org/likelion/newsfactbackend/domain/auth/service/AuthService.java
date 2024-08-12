package org.likelion.newsfactbackend.domain.auth.service;

import org.springframework.http.ResponseEntity;

public interface AuthService {
    /**
     * 소셜 로그인 api 사용 o
     * @param authorizeCode
     * @param type
     * @return
     */
    ResponseEntity<?> signIn(String authorizeCode, String type);

    ResponseEntity<?> logOut(String token);
}
