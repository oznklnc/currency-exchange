package com.ozan.currency.exchange.mapper;


import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.entity.ExchangeRate;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ExchangeRateResponseMapperTest extends UnitTest {

    @InjectMocks
    private ExchangeRateResponseMapper exchangeRateResponseMapper;

    @Test
    void shouldReturnExchangeRateResponse() {
        // given
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .sourceCurrency(Currency.EUR)
                .targetCurrency(Currency.USD)
                .rate(BigDecimal.valueOf(1.2))
                .build();

        // when
        ExchangeRateResponse exchangeRateResponse = exchangeRateResponseMapper.apply(exchangeRate);

        // then
        assertThat(exchangeRateResponse).isNotNull();
        assertThat(exchangeRateResponse.getFrom()).isEqualTo(Currency.EUR);
        assertThat(exchangeRateResponse.getTo()).isEqualTo(Currency.USD);
        assertThat(exchangeRateResponse.getRate()).isEqualTo(exchangeRate.getRate());
    }

}