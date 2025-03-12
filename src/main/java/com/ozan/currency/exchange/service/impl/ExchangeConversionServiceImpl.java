package com.ozan.currency.exchange.service.impl;

import com.ozan.currency.exchange.caller.CurrencyExchangeApiCaller;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.entity.ConversionHistory;
import com.ozan.currency.exchange.mapper.ConversionHistoryMapper;
import com.ozan.currency.exchange.mapper.ExchangeConversionResponseMapper;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.request.ConversionRequest;
import com.ozan.currency.exchange.model.response.ExchangeConversionResponse;
import com.ozan.currency.exchange.repository.ConversionHistoryRepository;
import com.ozan.currency.exchange.service.ExchangeConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeConversionServiceImpl implements ExchangeConversionService {

    private final CurrencyExchangeApiCaller currencyExchangeApiCaller;
    private final ConversionHistoryRepository conversionHistoryRepository;
    private final ConversionHistoryMapper conversionHistoryMapper;
    private final ExchangeConversionResponseMapper exchangeConversionResponseMapper;

    @Override
    public ExchangeConversionResponse convert(ConversionRequest request) {
        Currency from = request.getFrom();
        Currency to = request.getTo();
        BigDecimal amount = request.getAmount();
        log.info("Converting {} {} to {}", amount, from, to);
        FixerRateResponse exchangeRates = currencyExchangeApiCaller.getExchangeRates(from, to);
        ConversionHistory conversionHistory = conversionHistoryRepository.save(
                conversionHistoryMapper.apply(request, exchangeRates)
        );
        return exchangeConversionResponseMapper.apply(conversionHistory);
    }
}
