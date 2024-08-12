package org.likelion.newsfactbackend.domain.news.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(NewsException.class)
    public ResponseEntity<ResponseErrorDto> NotFound(NewsException ex) {
        ResponseErrorDto responseErrorDto = new ResponseErrorDto(ex.getErrorCode(), ex.getError(), ex.getMessage());
        log.warn("ErrorCode: {},   ErrorMessage: {}", ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(ex.getErrorCode()).body(responseErrorDto);
    }
}
