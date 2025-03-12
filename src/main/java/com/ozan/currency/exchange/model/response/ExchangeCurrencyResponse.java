package com.ozan.currency.exchange.model.response;

import com.ozan.currency.exchange.model.Base;
import com.ozan.currency.exchange.model.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeCurrencyResponse extends Base {

    @Schema(
            description = "Currency from which conversion is done",
            example = "USD"
    )
    private Currency from;

    @Schema(
            description = "Currency from which conversion is done",
            example = "TRY"
    )
    private Currency to;
}
