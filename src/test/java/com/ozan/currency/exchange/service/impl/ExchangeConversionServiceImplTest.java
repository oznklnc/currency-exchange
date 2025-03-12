package com.ozan.currency.exchange.service.impl;

import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.caller.CurrencyExchangeApiCaller;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.entity.ConversionHistory;
import com.ozan.currency.exchange.mapper.ConversionHistoryMapper;
import com.ozan.currency.exchange.mapper.ExchangeConversionResponseMapper;
import com.ozan.currency.exchange.model.request.ConversionRequest;
import com.ozan.currency.exchange.model.response.ExchangeConversionResponse;
import com.ozan.currency.exchange.repository.ConversionHistoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ExchangeConversionServiceImplTest extends UnitTest {

    @Mock
    private CurrencyExchangeApiCaller currencyExchangeApiCaller;
    @Mock
    private ConversionHistoryRepository conversionHistoryRepository;
    @Mock
    private ConversionHistoryMapper conversionHistoryMapper;
    @Mock
    private ExchangeConversionResponseMapper exchangeConversionResponseMapper;
    @InjectMocks
    private ExchangeConversionServiceImpl exchangeConversionService;

    @Captor
    private ArgumentCaptor<ConversionHistory> conversionHistoryCaptor;

    @Test
    void shouldConvertAndReturnExchangeConversionResponse() {
        //Given
        ConversionRequest request = ConversionRequest.builder()
                .from("USD")
                .to("EUR")
                .amount(new BigDecimal("120"))
                .build();

        FixerRateResponse fixerRateResponse = FixerRateResponse.builder()
                .base(request.getFrom())
                .rates(Map.of(request.getTo(), BigDecimal.ONE))
                .build();
        ConversionHistory conversionHistory = ConversionHistory.builder()
                .sourceCurrency(request.getFrom())
                .targetCurrency(request.getTo())
                .build();
        ExchangeConversionResponse expectedResponse = new ExchangeConversionResponse();

        when(currencyExchangeApiCaller.getExchangeRates(request.getFrom(), request.getTo())).thenReturn(fixerRateResponse);
        when(conversionHistoryMapper.apply(request, fixerRateResponse)).thenReturn(conversionHistory);
        when(exchangeConversionResponseMapper.apply(conversionHistory)).thenReturn(expectedResponse);
        when(conversionHistoryRepository.save(any())).thenReturn(conversionHistory);

        //When
        ExchangeConversionResponse response = exchangeConversionService.convert(request);

        //Then
        assertThat(response).isEqualTo(expectedResponse);
        InOrder inOrder = Mockito.inOrder(currencyExchangeApiCaller, conversionHistoryMapper, exchangeConversionResponseMapper, conversionHistoryRepository);
        inOrder.verify(currencyExchangeApiCaller).getExchangeRates(request.getFrom(), request.getTo());
        inOrder.verify(conversionHistoryMapper).apply(request, fixerRateResponse);
        inOrder.verify(conversionHistoryRepository).save(conversionHistoryCaptor.capture());
        inOrder.verify(exchangeConversionResponseMapper).apply(conversionHistory);
        assertThat(conversionHistoryCaptor.getValue()).isEqualTo(conversionHistory);
    }
}