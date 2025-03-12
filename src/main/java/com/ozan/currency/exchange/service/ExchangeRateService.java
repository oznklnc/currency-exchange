package com.ozan.currency.exchange.service;

import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeCurrencyListResponse;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;

public interface ExchangeRateService {

    ExchangeCurrencyListResponse getAllCurrencies();

    ExchangeRateResponse getExchangeRate(Currency from, Currency to);
}
