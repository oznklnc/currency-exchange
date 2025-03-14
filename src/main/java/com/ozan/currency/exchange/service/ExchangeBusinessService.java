package com.ozan.currency.exchange.service;

import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.request.ConversionRequest;
import com.ozan.currency.exchange.model.request.ExchangeConversionHistoryFilterRequest;
import com.ozan.currency.exchange.model.response.*;

public interface ExchangeBusinessService {

    ExchangeCurrencyListResponse getCurrencyList();

    ExchangeRateResponse getExchangeRate(Currency from, Currency to);

    ExchangeConversionResponse convert(ConversionRequest request);

    PagedResponse<ExchangeConversionHistoryResponse> getExchangeConversionHistory(ExchangeConversionHistoryFilterRequest request);
}
