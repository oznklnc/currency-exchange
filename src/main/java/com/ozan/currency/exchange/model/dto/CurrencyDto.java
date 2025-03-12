package com.ozan.currency.exchange.model.dto;

import com.ozan.currency.exchange.model.Base;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Schema(
        name = "CurrencyDto",
        description = "Schema to hold currency information"
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CurrencyDto extends Base {

    @Schema(
            description = "Currency code",
            example = "USD"
    )
    private String currencyCode;

    @Schema(
            description = "Currency definition",
            example = "United States Dollar"
    )
    private String definition;
}
