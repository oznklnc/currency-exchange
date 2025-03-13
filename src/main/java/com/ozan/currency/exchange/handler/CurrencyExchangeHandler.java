package com.ozan.currency.exchange.handler;

import com.ozan.currency.exchange.exception.ApiException;
import com.ozan.currency.exchange.exception.ClientException;
import com.ozan.currency.exchange.model.dto.ApiErrorDto;
import com.ozan.currency.exchange.model.dto.ErrorDto;
import com.ozan.currency.exchange.model.enums.ErrorCode;
import com.ozan.currency.exchange.util.MessageSourceUtil;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CurrencyExchangeHandler extends ResponseEntityExceptionHandler {

    private final MessageSourceUtil messageSourceUtil;


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.error("MethodArgumentNotValidException occurred message: {}, status:{}, headers: {}", formatExceptionMessage(ex), status.value(), headers.toString());
        List<ErrorDto> errorDtos = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::buildErrorDto)
                .toList();

        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .url(request.getDescription(false))
                .errors(errorDtos)
                .build();

        return ResponseEntity.status(400)
                .body(apiErrorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handleGlobalException(Exception exception,
                                                             WebRequest webRequest) {
        String url = webRequest.getDescription(false);
        log.error("Exception occurred message: {}, url: {}", exception.getMessage(), url);
        ErrorCode internalServerError = ErrorCode.INTERNAL_SERVER_ERROR;
        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .url(url)
                .errors(List.of(ErrorDto.builder()
                        .message(messageSourceUtil.getMessage(internalServerError.getMessageCode()))
                        .build()))
                .build();
        return ResponseEntity
                .status(internalServerError.getHttpStatus())
                .body(apiErrorDto);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorDto> handleGlobalException(ApiException exception,
                                                             WebRequest webRequest) {
        String url = webRequest.getDescription(false);
        String message = messageSourceUtil.getMessage(exception.getErrorCode().getMessageCode());
        log.error("ApiException occurred message: {}, url: {}", message, url);

        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .url(url)
                .errors(List.of(ErrorDto.builder()
                        .message(message)
                        .build()))
                .build();
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(apiErrorDto);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ApiErrorDto> handleGlobalException(ClientException exception,
                                                             WebRequest webRequest) {
        String url = webRequest.getDescription(false);
        String message = messageSourceUtil.getMessage(exception.getErrorCode().getMessageCode());
        log.error("ClientException occurred message: {}, url: {}", message, url);
        ErrorCode internalServerError = exception.getErrorCode();
        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .url(url)
                .errors(List.of(ErrorDto.builder()
                        .message(message)
                        .build()))
                .build();
        return ResponseEntity
                .status(internalServerError.getHttpStatus())
                .body(apiErrorDto);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ApiErrorDto> handleRateLimiterException(RequestNotPermitted ex,
                                                                  WebRequest webRequest) {
        String url = webRequest.getDescription(false);
        log.error("Rate limit exceeded message: {}, url: {}", ex.getMessage(), url);
        ErrorCode rateLimitExceeded = ErrorCode.RATE_LIMIT_EXCEEDED;
        ApiErrorDto apiError = ApiErrorDto.builder()
                .url(url)
                .errors(List.of(ErrorDto.builder()
                        .message(messageSourceUtil.getMessage(rateLimitExceeded.getMessageCode()))
                        .build()))
                .build();
        return ResponseEntity
                .status(rateLimitExceeded.getHttpStatus())
                .body(apiError);
    }

    private String formatExceptionMessage(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors()
                .stream()
                .map(a -> ((FieldError) a).getField() + " : " + a.getDefaultMessage())
                .collect(Collectors.joining(", ", "[", "]"));

    }


    private ErrorDto buildErrorDto(ObjectError error) {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = messageSourceUtil.getMessage(error.getDefaultMessage());
        return ErrorDto.builder()
                .field(fieldName)
                .message(errorMessage)
                .build();
    }
}
