package com.ozan.currency.exchange.mapper;


import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.entity.ConversionHistory;
import com.ozan.currency.exchange.generator.IdGenerator;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.request.ConversionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ConversionHistoryMapperTest extends UnitTest {

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private ConversionHistoryMapper mapper;

    @BeforeEach
    void setUp() {
        when(idGenerator.generateId()).thenReturn("testId");
    }

    @Test
    void shouldReturnConversionHistory() {
        //given
        ConversionRequest request = ConversionRequest.builder()
                .from("EUR")
                .to("TRY")
                .amount(BigDecimal.TEN)
                .build();

        FixerRateResponse fixerRateResponse = FixerRateResponse.builder()
                .success(true)
                .base(Currency.EUR)
                .rates(
                        Map.of(
                                Currency.TRY, new BigDecimal("38.454545")
                        )
                )
                .build();

        //when
        ConversionHistory conversionHistory = mapper.apply(request, fixerRateResponse);

        //then
        assertThat(conversionHistory).isNotNull();
        assertThat(conversionHistory.getTransactionId()).isEqualTo("testId");
        assertThat(conversionHistory.getSourceCurrency()).isEqualTo(Currency.EUR);
        assertThat(conversionHistory.getTargetCurrency()).isEqualTo(Currency.TRY);
        assertThat(conversionHistory.getAmount()).isNotNull();
        assertThat(conversionHistory.getConvertedAmount()).isNotNull();
    }

    @Test
    void shouldReturnConversionHistoryWithDefaultBaseCurrency() {
        //given
        ConversionRequest request = ConversionRequest.builder()
                .from("EUR")
                .to("TRY")
                .amount(BigDecimal.TEN)
                .build();

        FixerRateResponse fixerRateResponse = FixerRateResponse.builder()
                .success(true)
                .rates(
                        Map.of(
                                Currency.TRY, new BigDecimal("38.454545")
                        )
                )
                .build();

        //when
        ConversionHistory conversionHistory = mapper.apply(request, fixerRateResponse);

        //then
        assertThat(conversionHistory).isNotNull();
        assertThat(conversionHistory.getSourceCurrency()).isEqualTo(Currency.EUR);
    }
}