package com.ozan.currency.exchange.handler;

import com.ozan.currency.exchange.exception.ApiException;
import com.ozan.currency.exchange.exception.ClientException;
import com.ozan.currency.exchange.model.dto.ApiErrorDto;
import com.ozan.currency.exchange.model.dto.ErrorDto;
import com.ozan.currency.exchange.model.enums.ErrorCode;
import com.ozan.currency.exchange.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CurrencyExchangeHandler extends ResponseEntityExceptionHandler {

    private final MessageSourceUtil messageSourceUtil;


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.info("MethodArgumentNotValidException occurred message: {}, status:{}", ex.getMessage(), status.value());
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
        log.error("ApiException occurred message: {}, url: {}", exception.getMessage(), url);

        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .url(url)
                .errors(List.of(ErrorDto.builder()
                        .message(messageSourceUtil.getMessage(exception.getErrorCode().getMessageCode()))
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
        log.error("ClientException occurred message: {}, url: {}", exception.getMessage(), url);
        ErrorCode internalServerError = exception.getErrorCode();
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

    private ErrorDto buildErrorDto(ObjectError error) {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = messageSourceUtil.getMessage(error.getDefaultMessage());
        return ErrorDto.builder()
                .field(fieldName)
                .message(errorMessage)
                .build();
    }
}
