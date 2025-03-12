package com.ozan.currency.exchange.controller;

import com.ozan.currency.exchange.constant.UrlPath;
import com.ozan.currency.exchange.model.dto.ApiErrorDto;
import com.ozan.currency.exchange.model.request.ConversionRequest;
import com.ozan.currency.exchange.model.request.ExchangeConversionHistoryFilterRequest;
import com.ozan.currency.exchange.model.request.ExchangeRateRequest;
import com.ozan.currency.exchange.model.response.*;
import com.ozan.currency.exchange.service.ExchangeConversionHistoryService;
import com.ozan.currency.exchange.service.ExchangeConversionService;
import com.ozan.currency.exchange.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Currency Exchange APIs",
        description = "APIs to get exchange rate, convert currency and get all currencies"
)
@RestController
@Validated
@RequestMapping(UrlPath.BASE_PATH)
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeRateService exchangeRateService;
    private final ExchangeConversionService exchangeConversionService;
    private final ExchangeConversionHistoryService exchangeConversionHistoryService;

    @Operation(
            summary = "Get all currencies",
            description = "REST API to get all available currencies"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(
                            schema = @Schema(implementation = ExchangeCurrencyListResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDto.class)
                    )
            )
    })
    @GetMapping("/currencies")
    public ResponseEntity<ExchangeCurrencyListResponse> getAllCurrencies() {
        return ResponseEntity.ok(exchangeRateService.getAllCurrencies());
    }


    @Operation(
            summary = "Get exchange rate",
            description = "REST API to get exchange rate between two currencies"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(
                            schema = @Schema(implementation = ExchangeRateResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDto.class)
                    )
            )
    })
    @GetMapping("/exchange-rate")
    public ResponseEntity<ExchangeRateResponse> getExchangeRate(@Valid ExchangeRateRequest request) {
        return ResponseEntity.ok(exchangeRateService.getExchangeRate(request.getFrom(), request.getTo()));
    }


    @Operation(
            summary = "Convert currency",
            description = "REST API to convert currency from one to another"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(
                            schema = @Schema(implementation = ExchangeConversionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDto.class)
                    )
            )
    })
    @PostMapping("/convert")
    public ResponseEntity<ExchangeConversionResponse> convert(@RequestBody @Valid ConversionRequest request) {
        return ResponseEntity.ok(exchangeConversionService.convert(request));
    }


    @Operation(
            summary = "Get exchange conversion history",
            description = "REST API to get exchange conversion history"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(
                            schema = @Schema(implementation = PagedResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDto.class)
                    )
            )
    })
    @GetMapping("/conversion-history")
    public ResponseEntity<PagedResponse<ExchangeConversionHistoryResponse>> getExchangeConversionHistory(@Valid ExchangeConversionHistoryFilterRequest request) {
        return ResponseEntity
                .ok(exchangeConversionHistoryService.getExchangeConversionHistory(request));
    }

}
