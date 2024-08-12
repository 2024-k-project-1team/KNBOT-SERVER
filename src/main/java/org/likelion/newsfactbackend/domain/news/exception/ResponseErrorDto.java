package org.likelion.newsfactbackend.domain.news.exception;

public record ResponseErrorDto(
        int errorCode,
        ErrorCode error,
        String errorMessage
) {
}
