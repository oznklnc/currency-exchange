package com.ozan.currency.exchange.caller.fixer.model.response;

import com.ozan.currency.exchange.caller.fixer.model.Base;
import com.ozan.currency.exchange.model.enums.Currency;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FixerSymbolResponse extends Base {

    private Map<Currency, String> symbols;
}
