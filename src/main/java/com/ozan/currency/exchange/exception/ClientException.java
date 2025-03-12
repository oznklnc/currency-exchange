package com.ozan.currency.exchange.exception;

import com.ozan.currency.exchange.model.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public ClientException(String message, ErrorCode errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }
}
