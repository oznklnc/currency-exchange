package com.ozan.currency.exchange.service.impl;

import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.caller.CurrencyExchangeApiCaller;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerSymbolResponse;
import com.ozan.currency.exchange.mapper.ExchangeCurrencyListResponseMapper;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeCurrencyListResponse;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ExchangeRateServiceImplTest extends UnitTest {

    @Mock
    private CurrencyExchangeApiCaller currencyExchangeApiCaller;
    @Mock
    private ExchangeCurrencyListResponseMapper exchangeCurrencyListResponseMapper;
    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @Test
    void shouldReturnExchangeCurrencyListResponse() {
        //Given
        FixerSymbolResponse fixerSymbolResponse = new FixerSymbolResponse();
        ExchangeCurrencyListResponse expectedResponse = new ExchangeCurrencyListResponse();
        when(currencyExchangeApiCaller.getExchangeSymbols()).thenReturn(fixerSymbolResponse);
        when(exchangeCurrencyListResponseMapper.apply(fixerSymbolResponse)).thenReturn(expectedResponse);

        //When
        ExchangeCurrencyListResponse response = exchangeRateService.getAllCurrencies();

        //Then
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void shouldReturnExchangeRateResponse() {
        //Given
        BigDecimal rete = new BigDecimal("1.23232");
        Currency from = Currency.EUR;
        Currency to = Currency.USD;
        FixerRateResponse fixerRateResponse = FixerRateResponse.builder()
                .base(from)
                .rates(Map.of(to, rete))
                .build();
        when(currencyExchangeApiCaller.getExchangeRates(from, to)).thenReturn(fixerRateResponse);

        //When
        ExchangeRateResponse response = exchangeRateService.getExchangeRate(from, to);

        //Then
        assertThat(response).isNotNull();
        assertThat(response.getFrom()).isEqualTo(from);
        assertThat(response.getTo()).isEqualTo(to);
        assertThat(response.getRate()).isEqualTo(rete);
    }
}