package com.ozan.currency.exchange.model.request;

import com.ozan.currency.exchange.model.Base;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.validation.EnumValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Schema(
        name = "ExchangeRateRequest",
        description = "Schema to hold exchange rate request information"
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequest extends Base {


    @Schema(
            description = "Currency from which conversion is done",
            example = "USD"
    )
    @EnumValidation(enumClazz = Currency.class, message = "request.validation.invalid.currency")
    private String from;

    @Schema(
            description = "Currency to which conversion is done",
            example = "TRY"
    )
    @EnumValidation(enumClazz = Currency.class, message = "request.validation.invalid.currency")
    private String to;

    public Currency getFrom() {
        return Currency.valueOf(from);
    }

    public Currency getTo() {
        return Currency.valueOf(to);
    }
}
