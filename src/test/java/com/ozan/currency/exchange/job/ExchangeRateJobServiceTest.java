package com.ozan.currency.exchange.job;


import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.entity.ExchangeRate;
import com.ozan.currency.exchange.exception.ClientException;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.enums.ErrorCode;
import com.ozan.currency.exchange.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ExchangeRateJobServiceTest extends UnitTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ExchangeRateJobService exchangeRateJobService;

    @Captor
    private ArgumentCaptor<Map<Currency, BigDecimal>> fixerRatesCaptor;

    @Test
    void updateExchangeRatesTaskWhenToBeUpdatedExchangeRatesNotExist() {
        // Given
        when(exchangeRateService.getExchangeRateListToBeUpdated()).thenReturn(Collections.emptyList());

        // When
        exchangeRateJobService.updateExchangeRatesTask();

        // Then
        verifyNoMoreInteractions(exchangeRateService);
    }

    @Test
    void shouldUpdateExchangeRatesWithList() {
        // Given
        FixerRateResponse rateResponseForUSD = buildFixerRateResponseForUSD();
        FixerRateResponse rateResponseForEUR = buildFixerRateResponseForEUR();

        when(exchangeRateService.getExchangeRateListToBeUpdated()).thenReturn(buildExchangeRateList());
        when(exchangeRateService.getFixerRates(eq(Currency.USD), anyList())).thenReturn(rateResponseForUSD);
        when(exchangeRateService.getFixerRates(eq(Currency.EUR), anyList())).thenReturn(rateResponseForEUR);

        // When
        exchangeRateJobService.updateExchangeRatesTask();

        // Then
        verify(exchangeRateService).getExchangeRateListToBeUpdated();
        verify(exchangeRateService, times(2)).updateExchangeRates(anyMap(), anyList());
    }

    @Test
    void shouldUpdateExchangeRatesWithListWhenAnyExceptionFromExternalService() {
        // Given
        FixerRateResponse rateResponseForEUR = buildFixerRateResponseForEUR();
        ClientException clientException = new ClientException("Error occurred while calling external service", ErrorCode.CLIENT_4XX_ERROR);

        when(exchangeRateService.getExchangeRateListToBeUpdated()).thenReturn(buildExchangeRateList());
        when(exchangeRateService.getFixerRates(eq(Currency.USD), anyList())).thenThrow(clientException);
        when(exchangeRateService.getFixerRates(eq(Currency.EUR), anyList())).thenReturn(rateResponseForEUR);

        // When
        exchangeRateJobService.updateExchangeRatesTask();

        // Then
        verify(exchangeRateService).getExchangeRateListToBeUpdated();
        verify(exchangeRateService, times(1)).updateExchangeRates(fixerRatesCaptor.capture(), anyList());

        Map<Currency, BigDecimal> fixerRates = fixerRatesCaptor.getValue();
        assertThat(fixerRates)
                .isNotNull()
                .hasSize(1)
                .containsAnyOf(
                        entry(Currency.TRY, new BigDecimal("8.88"))
                );
    }

    private FixerRateResponse buildFixerRateResponseForUSD() {
        return FixerRateResponse.builder()
                .rates(Map.of(
                        Currency.EUR, new BigDecimal("0.85"),
                        Currency.TRY, new BigDecimal("7.54")
                ))
                .build();
    }

    private FixerRateResponse buildFixerRateResponseForEUR() {
        return FixerRateResponse.builder()
                .rates(Map.of(
                        Currency.TRY, new BigDecimal("8.88")
                ))
                .build();
    }

    private List<ExchangeRate> buildExchangeRateList() {
        ExchangeRate usdToTry = ExchangeRate.builder()
                .sourceCurrency(Currency.USD)
                .targetCurrency(Currency.TRY)
                .build();
        ExchangeRate usdToEur = ExchangeRate.builder()
                .sourceCurrency(Currency.USD)
                .targetCurrency(Currency.EUR)
                .build();

        ExchangeRate eurToTry = ExchangeRate.builder()
                .sourceCurrency(Currency.EUR)
                .targetCurrency(Currency.TRY)
                .build();

        return List.of(eurToTry, usdToEur, usdToTry);
    }

}