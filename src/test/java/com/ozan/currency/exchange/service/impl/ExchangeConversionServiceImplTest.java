package com.ozan.currency.exchange.service.impl;

import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.mapper.ExchangeConversionResponseMapper;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeConversionResponse;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExchangeConversionServiceImplTest extends UnitTest {

    @Mock
    private ExchangeConversionResponseMapper exchangeConversionResponseMapper;

    @InjectMocks
    private ExchangeConversionServiceImpl exchangeConversionService;

    @Test
    void shouldConvert() {
        //Given
        ExchangeRateResponse exchangeRate = ExchangeRateResponse.builder()
                .from(Currency.EUR)
                .to(Currency.TRY)
                .rate(new BigDecimal("38.454545"))
                .build();
        BigDecimal amount = BigDecimal.ONE;

        ExchangeConversionResponse expectedResponse = ExchangeConversionResponse.builder()
                .from(Currency.EUR)
                .to(Currency.TRY)
                .amount(BigDecimal.TEN)
                .convertedAmount(new BigDecimal("384.454545"))
                .build();

        when(exchangeConversionResponseMapper.apply(exchangeRate, amount))
                .thenReturn(expectedResponse);

        //When
        ExchangeConversionResponse actualResponse = exchangeConversionService.convert(exchangeRate, amount);

        //Then
        assertThat(actualResponse).isNotNull().isEqualTo(expectedResponse);
        verify(exchangeConversionResponseMapper).apply(exchangeRate, amount);
    }
}