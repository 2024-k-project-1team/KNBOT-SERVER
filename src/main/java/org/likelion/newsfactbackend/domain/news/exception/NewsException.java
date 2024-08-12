package org.likelion.newsfactbackend.domain.news.exception;

import lombok.Getter;

@Getter
public class NewsException extends RuntimeException {

    private final ErrorCode error;
    private final int errorCode;
    private final String errorMessage;


    public NewsException(ErrorCode error) {
        super(error.getErrorMessage());
        this.error = error;
        this.errorCode = error.getStateCode();
        this.errorMessage = error.getErrorMessage();
    }
}
