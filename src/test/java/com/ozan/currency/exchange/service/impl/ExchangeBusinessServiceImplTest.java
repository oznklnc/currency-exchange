package com.ozan.currency.exchange.service.impl;

import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.request.ConversionRequest;
import com.ozan.currency.exchange.model.request.ExchangeConversionHistoryFilterRequest;
import com.ozan.currency.exchange.model.response.*;
import com.ozan.currency.exchange.service.ExchangeConversionHistoryService;
import com.ozan.currency.exchange.service.ExchangeConversionService;
import com.ozan.currency.exchange.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExchangeBusinessServiceImplTest extends UnitTest {

    @Mock
    private ExchangeConversionHistoryService exchangeConversionHistoryService;
    @Mock
    private ExchangeConversionService exchangeConversionService;
    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ExchangeBusinessServiceImpl exchangeBusinessService;

    @Test
    void shouldGetCurrencyList() {
        // Given
        ExchangeCurrencyListResponse expectedResponse = new ExchangeCurrencyListResponse();
        when(exchangeRateService.getAllCurrencies()).thenReturn(expectedResponse);

        // When
        ExchangeCurrencyListResponse actualResponse = exchangeBusinessService.getCurrencyList();

        // Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void shouldGetExchangeRate() {
        // Given
        ExchangeRateResponse expectedResponse = new ExchangeRateResponse();
        when(exchangeRateService.getExchangeRate(Currency.EUR, Currency.USD)).thenReturn(expectedResponse);

        // When
        ExchangeRateResponse actualResponse = exchangeBusinessService.getExchangeRate(Currency.EUR, Currency.USD);

        // Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(exchangeRateService).getExchangeRate(Currency.EUR, Currency.USD);
    }

    @Test
    void shouldConvert() {
        // Given
        ConversionRequest request = ConversionRequest.builder()
                .from("EUR")
                .to("USD")
                .amount(new BigDecimal("100.0"))
                .build();
        ExchangeRateResponse exchangeRate = new ExchangeRateResponse();
        ExchangeConversionResponse expectedResponse = new ExchangeConversionResponse();

        when(exchangeRateService.getExchangeRate(request.getFrom(), request.getTo())).thenReturn(exchangeRate);
        when(exchangeConversionService.convert(exchangeRate, request.getAmount())).thenReturn(expectedResponse);

        // When
        ExchangeConversionResponse actualResponse = exchangeBusinessService.convert(request);

        // Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(exchangeRateService).getExchangeRate(request.getFrom(), request.getTo());
        verify(exchangeConversionService).convert(exchangeRate, request.getAmount());
        verify(exchangeConversionHistoryService).creteExchangeConversionHistory(expectedResponse);
    }

    @Test
    void shouldGetExchangeConversionHistory() {
        // Given
        ExchangeConversionHistoryFilterRequest request = ExchangeConversionHistoryFilterRequest.builder()
                .transactionId("1")
                .transactionDate(LocalDate.of(2025, 1, 1))
                .pageNumber(0)
                .pageSize(5)
                .build();
        PagedResponse<ExchangeConversionHistoryResponse> expectedResponse = new PagedResponse<>();
        when(exchangeConversionHistoryService.getExchangeConversionHistory(request)).thenReturn(expectedResponse);

        // When
        PagedResponse<ExchangeConversionHistoryResponse> actualResponse = exchangeBusinessService.getExchangeConversionHistory(request);

        // Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(exchangeConversionHistoryService).getExchangeConversionHistory(request);
    }
}