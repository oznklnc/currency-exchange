package com.ozan.currency.exchange.mapper;

import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.entity.ConversionHistory;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeConversionResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ExchangeConversionResponseMapperTest extends UnitTest {

    @InjectMocks
    private ExchangeConversionResponseMapper mapper;

    @Test
    void shouldReturnExchangeConversionResponse() {
        //Given
        ConversionHistory conversionHistory = ConversionHistory.builder()
                .transactionId("123")
                .sourceCurrency(Currency.USD)
                .targetCurrency(Currency.EUR)
                .amount(BigDecimal.TEN)
                .convertedAmount(BigDecimal.TEN)
                .build();

        //When
        ExchangeConversionResponse response = mapper.apply(conversionHistory);

        //Then
        assertThat(response).isNotNull();
        assertThat(response.getTransactionId()).isEqualTo(conversionHistory.getTransactionId());
        assertThat(response.getFrom()).isEqualTo(conversionHistory.getSourceCurrency());
        assertThat(response.getTo()).isEqualTo(conversionHistory.getTargetCurrency());
        assertThat(response.getAmount()).isEqualTo(conversionHistory.getAmount());
        assertThat(response.getConvertedAmount()).isEqualTo(conversionHistory.getConvertedAmount());
    }
}