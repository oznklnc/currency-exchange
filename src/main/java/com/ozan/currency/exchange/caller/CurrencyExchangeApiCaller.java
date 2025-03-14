package com.ozan.currency.exchange.caller;

import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerSymbolResponse;
import com.ozan.currency.exchange.model.enums.Currency;

import java.util.List;

public interface CurrencyExchangeApiCaller {

    FixerSymbolResponse getExchangeSymbols();

    FixerRateResponse getExchangeRates(Currency base, Currency target);

    FixerRateResponse getExchangeRatesWithTargets(Currency base, List<Currency> targets);
}
