package com.ozan.currency.exchange.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "business.rate.limit.exceeded"),
    RETRY_GENERIC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "business.retry.error"),
    FIXER_GENERIC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "business.fixer.error"),
    CLIENT_4XX_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "business.client.error"),
    CLIENT_5XX_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "business.client.server.error"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "business.validation.internal.server.error"),;

    private final HttpStatus httpStatus;
    private final String messageCode;
}
