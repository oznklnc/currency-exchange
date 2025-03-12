package com.ozan.currency.exchange.caller.currency.exchange;

import com.ozan.currency.exchange.model.response.CurrencyResponse;

public interface CurrencyExchangeApiCaller {

    CurrencyResponse getExchangeSymbols();
}
