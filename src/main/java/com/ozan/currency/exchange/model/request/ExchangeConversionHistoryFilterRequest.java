package com.ozan.currency.exchange.model.request;

import com.ozan.currency.exchange.model.Base;
import com.ozan.currency.exchange.validation.ConversionHistoryFilterValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Schema(
        name = "ExchangeConversionHistoryFilterRequest",
        description = "Request object to filter conversion history")
@Getter
@Setter
@SuperBuilder
@ConversionHistoryFilterValidation
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeConversionHistoryFilterRequest extends Base {

    @Schema(
            description = "Transaction id to filter",
            example = "1"
    )
    private String transactionId;

    @Schema(
            description = "Transaction date to filter",
            example = "2025-01-01"
    )
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate transactionDate;

    @Schema(
            description = "Page number to get",
            example = "0"
    )
    @Builder.Default
    private Integer pageNumber = 0;

    @Schema(
            description = "Page size to get",
            example = "5"
    )
    @Builder.Default
    private Integer pageSize = 5;

    public LocalDateTime getStartOfTransactionDate() {
        return Optional.ofNullable(transactionDate).map(LocalDate::atStartOfDay).orElse(null);
    }

    public LocalDateTime getEndOfTransactionDate() {
        return Optional.ofNullable(transactionDate).map(t -> t.atStartOfDay().plusDays(1).minusNanos(1)).orElse(null);
    }

}
