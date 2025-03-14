package com.ozan.currency.exchange.mapper;

import com.ozan.currency.exchange.generator.IdGenerator;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeConversionResponse;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class ExchangeConversionResponseMapper
        implements BiFunction<ExchangeRateResponse, BigDecimal, ExchangeConversionResponse> {

    private final IdGenerator idGenerator;

    @Override
    public ExchangeConversionResponse apply(ExchangeRateResponse exchangeRateResponse, BigDecimal amount) {
        Currency from = exchangeRateResponse.getFrom();
        Currency to = exchangeRateResponse.getTo();
        BigDecimal rate = exchangeRateResponse.getRate();
        BigDecimal convertedAmount = amount
                .multiply(rate).setScale(8, RoundingMode.HALF_UP);
        return ExchangeConversionResponse.builder()
                .transactionId(idGenerator.generateId())
                .from(from)
                .to(to)
                .amount(amount)
                .convertedAmount(convertedAmount)
                .build();
    }
}
