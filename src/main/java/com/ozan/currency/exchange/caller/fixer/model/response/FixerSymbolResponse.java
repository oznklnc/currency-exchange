package com.ozan.currency.exchange.caller.fixer.model.response;

import com.ozan.currency.exchange.caller.fixer.model.Base;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SymbolResponse extends Base {

    private Map<String, String> symbols;
}
