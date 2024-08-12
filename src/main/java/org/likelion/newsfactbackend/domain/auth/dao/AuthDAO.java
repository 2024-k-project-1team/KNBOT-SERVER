package org.likelion.newsfactbackend.domain.auth.dao;

import org.likelion.newsfactbackend.domain.auth.dto.request.RequestSaveUserDto;
import org.springframework.http.ResponseEntity;


public interface AuthDAO {
    /**
     * 유저 DB 조회 후 token 생성
     * refresh token redis {name : token} save
     * 닉네임 중복 여부 확인 후 저장
     * @param requestSaveUserDto
     * @return JWT access token
     */
    ResponseEntity<?> login(RequestSaveUserDto requestSaveUserDto);

    /**
     * refresh 토큰 삭제
     * redis storage {access token : logout} save
     * @param token
     * @return
     */
    ResponseEntity<?> deleteToken(String token);
}
