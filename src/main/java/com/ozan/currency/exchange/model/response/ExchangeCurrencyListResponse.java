package com.ozan.currency.exchange.model.response;

import com.ozan.currency.exchange.model.Base;
import com.ozan.currency.exchange.model.dto.CurrencyDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeCurrencyResponse extends Base {

    private List<CurrencyDto> currencies;
}
