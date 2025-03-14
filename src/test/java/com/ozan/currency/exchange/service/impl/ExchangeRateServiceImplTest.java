package com.ozan.currency.exchange.service.impl;

import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.caller.CurrencyExchangeApiCaller;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerSymbolResponse;
import com.ozan.currency.exchange.configuration.properties.JobConfigProperties;
import com.ozan.currency.exchange.entity.ExchangeRate;
import com.ozan.currency.exchange.mapper.ExchangeCurrencyListResponseMapper;
import com.ozan.currency.exchange.mapper.ExchangeRateResponseMapper;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeCurrencyListResponse;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;
import com.ozan.currency.exchange.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExchangeRateServiceImplTest extends UnitTest {

    @Mock
    private CurrencyExchangeApiCaller currencyExchangeApiCaller;
    @Mock
    private ExchangeCurrencyListResponseMapper exchangeCurrencyListResponseMapper;
    @Mock
    private ExchangeRateResponseMapper exchangeRateResponseMapper;
    @Mock
    private ExchangeRateRepository exchangeRateRepository;
    @Mock
    private JobConfigProperties jobConfigProperties;
    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @Captor
    private ArgumentCaptor<List<ExchangeRate>> exchangeRateListCaptor;
    @Captor
    private ArgumentCaptor<ExchangeRate> exchangeRateArgumentCaptor;

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
    void shouldReturnExchangeRateResponseWhenExchangeRateNotExistInDb() {
        //Given
        BigDecimal rete = new BigDecimal("1.23232");
        Currency from = Currency.EUR;
        Currency to = Currency.USD;
        FixerRateResponse fixerRateResponse = FixerRateResponse.builder()
                .base(from)
                .rates(Map.of(to, rete))
                .build();
        ExchangeRateResponse expectedResponse = new ExchangeRateResponse();
        when(currencyExchangeApiCaller.getExchangeRates(from, to)).thenReturn(fixerRateResponse);
        when(exchangeRateRepository.findBySourceCurrencyAndTargetCurrency(from, to))
                .thenReturn(Optional.empty());
        when(jobConfigProperties.getValidityPeriod()).thenReturn(30L);
        when(exchangeRateRepository.save(any(ExchangeRate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(exchangeRateResponseMapper.apply(any(ExchangeRate.class)))
                .thenReturn(expectedResponse);

        //When
        ExchangeRateResponse response = exchangeRateService.getExchangeRate(from, to);

        //Then
        assertThat(response).isNotNull();
        verify(currencyExchangeApiCaller).getExchangeRates(from, to);
        verify(exchangeRateRepository).findBySourceCurrencyAndTargetCurrency(from, to);
        verify(exchangeRateRepository).save(exchangeRateArgumentCaptor.capture());

        ExchangeRate savedExchangeRate = exchangeRateArgumentCaptor.getValue();
        assertThat(savedExchangeRate.getSourceCurrency()).isEqualTo(from);
        assertThat(savedExchangeRate.getTargetCurrency()).isEqualTo(to);
        assertThat(savedExchangeRate.getRate()).isEqualTo(rete);
        assertThat(savedExchangeRate.getValidDate()).isAfter(LocalDateTime.now());
    }

    @Test
    void shouldUpdateExchangeRateAndReturnExchangeRateResponseWhenExchangeRateExistInDb() {
        //Given
        BigDecimal rete = new BigDecimal("1.23232");
        Currency from = Currency.EUR;
        Currency to = Currency.USD;
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .sourceCurrency(from)
                .targetCurrency(to)
                .rate(new BigDecimal("1.0"))
                .validDate(LocalDateTime.now().minusMinutes(50L))
                .build();
        FixerRateResponse fixerRateResponse = FixerRateResponse.builder()
                .base(from)
                .rates(Map.of(to, rete))
                .build();
        ExchangeRateResponse expectedResponse = new ExchangeRateResponse();

        when(currencyExchangeApiCaller.getExchangeRates(from, to)).thenReturn(fixerRateResponse);
        when(exchangeRateRepository.findBySourceCurrencyAndTargetCurrency(from, to))
                .thenReturn(Optional.of(exchangeRate));
        when(jobConfigProperties.getValidityPeriod()).thenReturn(30L);
        when(exchangeRateRepository.save(any(ExchangeRate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(exchangeRateResponseMapper.apply(any(ExchangeRate.class)))
                .thenReturn(expectedResponse);

        //When
        ExchangeRateResponse response = exchangeRateService.getExchangeRate(from, to);

        //Then
        assertThat(response).isNotNull();
        verify(currencyExchangeApiCaller).getExchangeRates(from, to);
        verify(exchangeRateRepository).findBySourceCurrencyAndTargetCurrency(from, to);
        verify(exchangeRateRepository).save(exchangeRateArgumentCaptor.capture());

        ExchangeRate savedExchangeRate = exchangeRateArgumentCaptor.getValue();
        assertThat(savedExchangeRate.getSourceCurrency()).isEqualTo(from);
        assertThat(savedExchangeRate.getTargetCurrency()).isEqualTo(to);
        assertThat(savedExchangeRate.getRate()).isEqualTo(rete);
        assertThat(savedExchangeRate.getValidDate()).isAfter(LocalDateTime.now());
    }

    @Test
    void shouldReturnExchangeRateResponseWhenExchangeRateExistInDbAndValid() {
        //Given
        BigDecimal rete = new BigDecimal("1.23232");
        Currency from = Currency.EUR;
        Currency to = Currency.USD;
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .sourceCurrency(from)
                .targetCurrency(to)
                .rate(rete)
                .validDate(LocalDateTime.now().plusMinutes(50L))
                .build();
        ExchangeRateResponse expectedResponse = new ExchangeRateResponse();
        when(exchangeRateRepository.findBySourceCurrencyAndTargetCurrency(from, to))
                .thenReturn(Optional.of(exchangeRate));
        when(exchangeRateResponseMapper.apply(exchangeRate))
                .thenReturn(expectedResponse);

        //When
        ExchangeRateResponse response = exchangeRateService.getExchangeRate(from, to);

        //Then
        assertThat(response).isNotNull();
        verify(exchangeRateRepository).findBySourceCurrencyAndTargetCurrency(from, to);
        verify(exchangeRateResponseMapper).apply(exchangeRate);
    }

    @Test
    void getExchangeRateListToBeUpdated() {
        //Given
        Page<ExchangeRate> exchangeRatePage = new PageImpl<>(
                List.of(ExchangeRate.builder().build())
        );

        when(jobConfigProperties.getPageNumber()).thenReturn(0);
        when(jobConfigProperties.getFetchLimit()).thenReturn(10);
        when(exchangeRateRepository.findAllByValidDateBefore(any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(exchangeRatePage);

        //When
        List<ExchangeRate> exchangeRateList = exchangeRateService.getExchangeRateListToBeUpdated();

        //Then
        assertThat(exchangeRateList).isNotEmpty().hasSize(exchangeRatePage.getContent().size());
    }

    @Test
    void shouldGetFixerRates() {
        //Given
        Currency from = Currency.EUR;
        List<Currency> targets = List.of(Currency.USD, Currency.TRY);

        FixerRateResponse fixerRateResponse = FixerRateResponse.builder()
                .base(from)
                .rates(Map.of(Currency.USD, new BigDecimal("1.23232"), Currency.TRY, new BigDecimal("10.23232")))
                .build();
        when(currencyExchangeApiCaller.getExchangeRatesWithTargets(from, targets)).thenReturn(fixerRateResponse);

        //When
        FixerRateResponse response = exchangeRateService.getFixerRates(from, targets);

        //Then
        assertThat(response).isNotNull();
        assertThat(response.getBase()).isEqualTo(from);
        assertThat(response.getRates()).containsKeys(Currency.USD, Currency.TRY);
        verify(currencyExchangeApiCaller).getExchangeRatesWithTargets(from, targets);
    }

    @Test
    void shouldUpdateExchangeRates() {
        //Given
        Currency from = Currency.EUR;
        Currency to = Currency.USD;
        BigDecimal rate = new BigDecimal("1.23232");
        String updater = "updater";
        LocalDateTime validDate = LocalDateTime.now().minusMinutes(50L);
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .sourceCurrency(from)
                .targetCurrency(to)
                .rate(new BigDecimal("1.0"))
                .validDate(validDate)
                .build();
        List<ExchangeRate> exchangeRates = List.of(exchangeRate);
        Map<Currency, BigDecimal> rates = Map.of(to, rate);

        when(jobConfigProperties.getUpdaterName()).thenReturn(updater);

        //When
        exchangeRateService.updateExchangeRates(rates, exchangeRates);

        //Then
        verify(exchangeRateRepository).saveAll(exchangeRateListCaptor.capture());
        List<ExchangeRate> capturedExchangeRates = exchangeRateListCaptor.getValue();
        assertThat(capturedExchangeRates).isNotEmpty().hasSize(exchangeRates.size());
        ExchangeRate updatedExchangeRate = capturedExchangeRates.get(0);
        assertThat(updatedExchangeRate.getUpdatedBy()).isEqualTo(updater);
        assertThat(updatedExchangeRate.getRate()).isEqualTo(rate);
        assertThat(updatedExchangeRate.getValidDate()).isAfter(validDate);
    }
}