package com.ozan.currency.exchange.mapper;


import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.generator.IdGenerator;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeConversionResponse;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ExchangeConversionResponseMapperTest extends UnitTest {

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private ExchangeConversionResponseMapper mapper;

    @BeforeEach
    void setUp() {
        when(idGenerator.generateId()).thenReturn("testId");
    }

    @Test
    void shouldReturnConversionHistory() {
        //given
        ExchangeRateResponse exchangeRate = ExchangeRateResponse.builder()
                .from(Currency.EUR)
                .to(Currency.TRY)
                .rate(new BigDecimal("38.454545"))
                .build();
        BigDecimal amount = BigDecimal.ONE;

        //when
        ExchangeConversionResponse actualResponse = mapper.apply(exchangeRate, amount);

        //then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getTransactionId()).isEqualTo("testId");
        assertThat(actualResponse.getFrom()).isEqualTo(exchangeRate.getFrom());
        assertThat(actualResponse.getTo()).isEqualTo(exchangeRate.getTo());
        assertThat(actualResponse.getAmount()).isNotNull();
        assertThat(actualResponse.getConvertedAmount()).isNotNull();
    }

}