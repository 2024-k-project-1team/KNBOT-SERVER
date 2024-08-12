package org.likelion.newsfactbackend.domain.news.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_FOUND_NEWS_URL(404, "뉴스 URL을 찾을 수 없습니다."),
    NOT_FOUND_OID(404, "언론사를 찾을 수 없습니다."),
    EMPTY_SIZE(400, "Size 값이 필요합니다."),
    EMPTY_KEYWORD(400, "검색어를 값이 비었습니다. 검색어 값을 입력해주세요."),
    SERVER_ERROR(503, "검색 사용자 수가 많습니다. 다시 시도해주세요.");


    private final int stateCode;
    private final String errorMessage;

    ErrorCode(int code, String message) {
        this.stateCode = code;
        this.errorMessage = message;
    }
}
