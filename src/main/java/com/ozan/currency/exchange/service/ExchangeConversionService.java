package com.ozan.currency.exchange.service;

import com.ozan.currency.exchange.model.response.ExchangeConversionResponse;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;

import java.math.BigDecimal;

public interface ExchangeConversionService {

    ExchangeConversionResponse convert(ExchangeRateResponse exchangeRate, BigDecimal amount);
}
