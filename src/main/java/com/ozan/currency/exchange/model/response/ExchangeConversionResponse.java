package com.ozan.currency.exchange.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;


@Schema(
        name = "ExchangeConversionResponse",
        description = "Schema to hold exchange conversion response information"
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeConversionResponse extends ExchangeCurrencyResponse {

    @Schema(description = "Transaction id of the exchange conversion")
    private String transactionId;
    @Schema(description = "Amount to be converted")
    private BigDecimal amount;
    @Schema(description = "Converted amount")
    private BigDecimal convertedAmount;
}
