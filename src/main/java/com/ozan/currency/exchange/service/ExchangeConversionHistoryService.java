package com.ozan.currency.exchange.service;

import com.ozan.currency.exchange.model.request.ExchangeConversionHistoryFilterRequest;
import com.ozan.currency.exchange.model.response.ExchangeConversionHistoryResponse;
import com.ozan.currency.exchange.model.response.ExchangeConversionResponse;
import com.ozan.currency.exchange.model.response.PagedResponse;

public interface ExchangeConversionHistoryService {

    PagedResponse<ExchangeConversionHistoryResponse> getExchangeConversionHistory(ExchangeConversionHistoryFilterRequest request);

    void creteExchangeConversionHistory(ExchangeConversionResponse exchangeConversionResponse);
}
