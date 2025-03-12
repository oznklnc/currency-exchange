package com.ozan.currency.exchange.caller;

import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerSymbolResponse;
import com.ozan.currency.exchange.model.enums.Currency;

public interface CurrencyExchangeApiCaller {

    FixerSymbolResponse getExchangeSymbols();

    FixerRateResponse getExchangeRates(Currency base, Currency target);
}
