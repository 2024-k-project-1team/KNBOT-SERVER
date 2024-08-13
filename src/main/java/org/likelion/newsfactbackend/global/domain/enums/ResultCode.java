package org.likelion.newsfactbackend.global.domain.enums;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public enum ResultCode {
    OK(200, "성공"),
    FAIL(400,"실패"),
    UNAUTHORIZED(403, "권한 없음"),
    PASSWORD_NOT_MATCH(403, "비밀번호 불일치"),
    TOKEN_IS_NULL(401,"토큰값이 null 입니다."),
    DELETED_USER(401, "탈퇴 유저"),
    EXPIRED_TOKEN(401, "토큰 유효 기간 만료"),
    DUPLICATE_CHATROOM(400,"이미 채팅방이 존재합니다."),
    NOT_FOUND_CHATROOM_NAME(401,"채팅방 이름이 없습니다."),
    NOT_FOUND_CHATROOM(401,"채팅방을 찾지 못했습니다."),
    NOT_FOUND_CHATROOM_NUMBER(401,"채팅방번호를 입력해주세요."),
    DUPLICATION_NICKNAME(400, "이미 존재하는 닉네임입니다."),
    DUPLICATION_EMAIL(400, "이미 존재하는 이메일입니다."),
    NOT_IN_STORAGE(404, "스토리지에 저장되어 있지 않습니다."),
    NOT_FOUND_USER(401, "사용자를 찾을 수 없습니다.");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseEntity<String> toResponseEntity(){
        return ResponseEntity.status(this.code).body(this.message);
    }
}
