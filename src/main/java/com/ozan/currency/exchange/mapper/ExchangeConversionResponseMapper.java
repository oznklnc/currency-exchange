package com.ozan.currency.exchange.mapper;

import com.ozan.currency.exchange.entity.ConversionHistory;
import com.ozan.currency.exchange.model.response.ExchangeConversionResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ExchangeConversionResponseMapper
        implements Function<ConversionHistory, ExchangeConversionResponse> {

    @Override
    public ExchangeConversionResponse apply(ConversionHistory conversionHistory) {
        return ExchangeConversionResponse.builder()
                .transactionId(conversionHistory.getTransactionId())
                .from(conversionHistory.getSourceCurrency())
                .to(conversionHistory.getTargetCurrency())
                .amount(conversionHistory.getAmount())
                .convertedAmount(conversionHistory.getConvertedAmount())
                .build();
    }
}
