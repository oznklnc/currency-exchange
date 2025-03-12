package com.ozan.currency.exchange.model.request;

import com.ozan.currency.exchange.model.Base;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.validation.EnumValidation;
import com.ozan.currency.exchange.validation.PositiveBigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Schema(
        name = "ConversionRequest",
        description = "Schema to hold conversion request information"
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ConversionRequest extends Base {

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

    @NotNull(message = "request.validation.amount.not.null")
    @DecimalMin(value = "0.0", inclusive = false, message = "request.validation.amount.min")
    @Digits(integer = 10, fraction = 6, message = "request.validation.amount.digits")
    @PositiveBigDecimal
    private BigDecimal amount;


    public Currency getFrom() {
        return Currency.valueOf(from);
    }

    public Currency getTo() {
        return Currency.valueOf(to);
    }
}
