package com.ozan.currency.exchange.service;

import com.ozan.currency.exchange.model.request.ConversionRequest;
import com.ozan.currency.exchange.model.response.ExchangeConversionResponse;

public interface ExchangeConversionService {

    ExchangeConversionResponse convert(ConversionRequest request);
}
