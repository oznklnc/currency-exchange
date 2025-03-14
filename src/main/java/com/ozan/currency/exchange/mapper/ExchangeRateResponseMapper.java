package com.ozan.currency.exchange.mapper;

import com.ozan.currency.exchange.entity.ExchangeRate;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ExchangeRateResponseMapper implements Function<ExchangeRate, ExchangeRateResponse> {

    @Override
    public ExchangeRateResponse apply(ExchangeRate rate) {
        return  ExchangeRateResponse.builder()
                .from(rate.getSourceCurrency())
                .to(rate.getTargetCurrency())
                .rate(rate.getRate())
                .build();
    }
}
