package com.ozan.currency.exchange.service.impl;

import com.ozan.currency.exchange.caller.CurrencyExchangeApiCaller;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerSymbolResponse;
import com.ozan.currency.exchange.mapper.ExchangeCurrencyListResponseMapper;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeCurrencyListResponse;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;
import com.ozan.currency.exchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final CurrencyExchangeApiCaller currencyExchangeApiCaller;
    private final ExchangeCurrencyListResponseMapper exchangeCurrencyListResponseMapper;

    @Override
    public ExchangeCurrencyListResponse getAllCurrencies() {
        FixerSymbolResponse exchangeSymbols = currencyExchangeApiCaller.getExchangeSymbols();
        return exchangeCurrencyListResponseMapper.apply(exchangeSymbols);
    }

    @Override
    public ExchangeRateResponse getExchangeRate(Currency from, Currency to) {
        FixerRateResponse exchangeRates = currencyExchangeApiCaller.getExchangeRates(from, to);
        return ExchangeRateResponse.builder()
                .from(exchangeRates.getBase())
                .to(to)
                .rate(exchangeRates.getRates().get(to))
                .build();
    }

}
