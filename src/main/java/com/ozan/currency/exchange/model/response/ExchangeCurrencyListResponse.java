package com.ozan.currency.exchange.model.response;

import com.ozan.currency.exchange.model.Base;
import com.ozan.currency.exchange.model.dto.CurrencyDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Schema(
        name = "ExchangeCurrencyListResponse",
        description = "Schema to hold exchange currency list response information"
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeCurrencyListResponse extends Base {

    @Schema(
            description = "List of currencies"
    )
    private List<CurrencyDto> currencies;
}
