package com.ozan.currency.exchange.mapper;

import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.entity.ConversionHistory;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeConversionHistoryResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ExchangeConversionHistoryResponseMapperTest extends UnitTest {

    @InjectMocks
    private ExchangeConversionHistoryResponseMapper mapper;

    @Test
    void shouldReturnExchangeConversionHistoryResponse() {
        //given
        ConversionHistory conversionHistory = ConversionHistory.builder()
                .transactionId("testId")
                .sourceCurrency(Currency.EUR)
                .targetCurrency(Currency.TRY)
                .amount(BigDecimal.TEN)
                .convertedAmount(BigDecimal.TEN)
                .createdAt(LocalDateTime.now())
                .build();

        //when
        ExchangeConversionHistoryResponse exchangeConversionHistoryResponse = mapper.apply(conversionHistory);

        //then
        assertThat(exchangeConversionHistoryResponse).isNotNull();
        assertThat(exchangeConversionHistoryResponse.getTransactionId()).isEqualTo(conversionHistory.getTransactionId());
        assertThat(exchangeConversionHistoryResponse.getFrom()).isEqualTo(conversionHistory.getSourceCurrency());
        assertThat(exchangeConversionHistoryResponse.getTo()).isEqualTo(conversionHistory.getTargetCurrency());
        assertThat(exchangeConversionHistoryResponse.getAmount()).isEqualTo(conversionHistory.getAmount());
        assertThat(exchangeConversionHistoryResponse.getConvertedAmount()).isEqualTo(conversionHistory.getConvertedAmount());
        assertThat(exchangeConversionHistoryResponse.getTransactionDate()).isEqualTo(conversionHistory.getCreatedAt());
    }
}