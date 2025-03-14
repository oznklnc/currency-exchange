package com.ozan.currency.exchange.service;

import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.entity.ExchangeRate;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeCurrencyListResponse;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ExchangeRateService {

    ExchangeCurrencyListResponse getAllCurrencies();

    ExchangeRateResponse getExchangeRate(Currency from, Currency to);

    List<ExchangeRate> getExchangeRateListToBeUpdated();

    FixerRateResponse getFixerRates(Currency from, List<Currency> targets);

    void updateExchangeRates(Map<Currency, BigDecimal> rates, List<ExchangeRate> exchangeRates);
}
