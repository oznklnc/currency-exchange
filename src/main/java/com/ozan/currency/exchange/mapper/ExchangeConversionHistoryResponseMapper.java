package com.ozan.currency.exchange.mapper;

import com.ozan.currency.exchange.entity.ConversionHistory;
import com.ozan.currency.exchange.model.response.ExchangeConversionHistoryResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ExchangeConversionHistoryResponseMapper
        implements Function<ConversionHistory, ExchangeConversionHistoryResponse> {

    @Override
    public ExchangeConversionHistoryResponse apply(ConversionHistory conversionHistory) {
        return ExchangeConversionHistoryResponse.builder()
                .transactionId(conversionHistory.getTransactionId())
                .from(conversionHistory.getSourceCurrency())
                .to(conversionHistory.getTargetCurrency())
                .amount(conversionHistory.getAmount())
                .convertedAmount(conversionHistory.getConvertedAmount())
                .transactionDate(conversionHistory.getCreatedAt())
                .build();
    }
}
