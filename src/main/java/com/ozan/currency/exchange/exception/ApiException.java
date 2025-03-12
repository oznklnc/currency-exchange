package com.ozan.currency.exchange.exception;

import com.ozan.currency.exchange.model.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessageCode());
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

    public ApiException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getMessageCode());
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
