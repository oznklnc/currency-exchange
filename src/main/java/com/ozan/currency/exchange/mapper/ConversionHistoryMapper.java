package com.ozan.currency.exchange.mapper;

import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.entity.ConversionHistory;
import com.ozan.currency.exchange.generator.IdGenerator;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.request.ConversionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class ConversionHistoryMapper
        implements BiFunction<ConversionRequest, FixerRateResponse, ConversionHistory> {

    private final IdGenerator idGenerator;

    @Override
    public ConversionHistory apply(ConversionRequest request, FixerRateResponse fixerRateResponse) {
        Currency to = request.getTo();
        BigDecimal rate = fixerRateResponse.getRates().get(to);
        BigDecimal amount = request.getAmount();
        BigDecimal convertedAmount = amount
                .multiply(rate).setScale(8, RoundingMode.HALF_UP);

        return ConversionHistory.builder()
                .sourceCurrency(Optional.ofNullable(fixerRateResponse.getBase())
                        .orElse(request.getFrom())
                )
                .transactionId(idGenerator.generateId())
                .targetCurrency(to)
                .amount(amount)
                .convertedAmount(convertedAmount)
                .build();
    }
}
