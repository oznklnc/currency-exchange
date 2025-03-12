package com.ozan.currency.exchange.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Schema(
        name = "ExchangeRateResponse",
        description = "Schema to hold exchange rate response information"
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse extends ExchangeCurrencyResponse {

    @Schema(description = "Rate of the exchange value")
    private BigDecimal rate;
}
