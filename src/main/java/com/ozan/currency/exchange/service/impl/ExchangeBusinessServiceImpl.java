package com.ozan.currency.exchange.service.impl;

import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.request.ConversionRequest;
import com.ozan.currency.exchange.model.request.ExchangeConversionHistoryFilterRequest;
import com.ozan.currency.exchange.model.response.*;
import com.ozan.currency.exchange.service.ExchangeBusinessService;
import com.ozan.currency.exchange.service.ExchangeConversionHistoryService;
import com.ozan.currency.exchange.service.ExchangeConversionService;
import com.ozan.currency.exchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExchangeBusinessServiceImpl implements ExchangeBusinessService {

    private final ExchangeConversionHistoryService exchangeConversionHistoryService;
    private final ExchangeConversionService exchangeConversionService;
    private final ExchangeRateService exchangeRateService;

    @Override
    public ExchangeCurrencyListResponse getCurrencyList() {
        return exchangeRateService.getAllCurrencies();
    }

    @Override
    public ExchangeRateResponse getExchangeRate(Currency from, Currency to) {
        return exchangeRateService.getExchangeRate(from, to);
    }

    @Transactional
    @Override
    public ExchangeConversionResponse convert(ConversionRequest request) {
        ExchangeRateResponse exchangeRate = exchangeRateService.getExchangeRate(request.getFrom(), request.getTo());
        ExchangeConversionResponse exchangeConversionResponse = exchangeConversionService.convert(exchangeRate, request.getAmount());
        exchangeConversionHistoryService.creteExchangeConversionHistory(exchangeConversionResponse);
        return exchangeConversionResponse;
    }

    @Override
    public PagedResponse<ExchangeConversionHistoryResponse> getExchangeConversionHistory(ExchangeConversionHistoryFilterRequest request) {
        return exchangeConversionHistoryService.getExchangeConversionHistory(request);
    }
}
