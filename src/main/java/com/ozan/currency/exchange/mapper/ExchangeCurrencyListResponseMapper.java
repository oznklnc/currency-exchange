package com.ozan.currency.exchange.mapper;

import com.ozan.currency.exchange.caller.fixer.model.response.FixerSymbolResponse;
import com.ozan.currency.exchange.model.dto.CurrencyDto;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeCurrencyListResponse;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ExchangeCurrencyListResponseMapper
        implements Function<FixerSymbolResponse, ExchangeCurrencyListResponse> {

    @Override
    public ExchangeCurrencyListResponse apply(FixerSymbolResponse fixerSymbolResponse) {
        List<CurrencyDto> currencyList = fixerSymbolResponse.getSymbols().entrySet()
                .stream()
                .map(this::buildCurrencyDto)
                .sorted(Comparator.comparing(CurrencyDto::getCurrencyCode))
                .collect(Collectors.toList());
        return ExchangeCurrencyListResponse.builder()
                .currencies(currencyList)
                .build();
    }

    private CurrencyDto buildCurrencyDto(Map.Entry<Currency, String> symbol) {
        return CurrencyDto.builder()
                .currencyCode(symbol.getKey().getCode())
                .definition(symbol.getValue())
                .build();
    }
}
