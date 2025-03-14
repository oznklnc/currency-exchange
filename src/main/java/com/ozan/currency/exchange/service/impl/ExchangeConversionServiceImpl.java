package com.ozan.currency.exchange.service.impl;

import com.ozan.currency.exchange.mapper.ExchangeConversionResponseMapper;
import com.ozan.currency.exchange.model.response.ExchangeConversionResponse;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;
import com.ozan.currency.exchange.service.ExchangeConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeConversionServiceImpl implements ExchangeConversionService {

    private final ExchangeConversionResponseMapper exchangeConversionResponseMapper;

    @Override
    public ExchangeConversionResponse convert(ExchangeRateResponse exchangeRate, BigDecimal amount) {
        log.info("Converting {} {} to {} with rate:{}", amount, exchangeRate.getFrom(), exchangeRate.getTo(), exchangeRate.getRate());
        return exchangeConversionResponseMapper.apply(exchangeRate, amount);
    }
}
