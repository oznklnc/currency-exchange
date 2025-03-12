package com.ozan.currency.exchange.mapper;

import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.entity.ConversionHistory;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeConversionHistoryResponse;
import com.ozan.currency.exchange.model.response.PagedResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseMapperTest extends UnitTest {

    @Mock
    private ExchangeConversionHistoryResponseMapper exchangeConversionHistoryResponseMapper;

    @InjectMocks
    private ResponseMapper responseMapper;

    @Test
    void shouldReturnPagedResponse() {
        //Given
        ConversionHistory conversionHistory = ConversionHistory.builder()
                .transactionId("123")
                .targetCurrency(Currency.USD)
                .sourceCurrency(Currency.TRY)
                .amount(BigDecimal.TEN)
                .convertedAmount(BigDecimal.TEN)
                .id(1L)
                .createdAt(LocalDateTime.now())
                .build();
        Page<ConversionHistory> conversionHistoryPage = new PageImpl<>(
                List.of(conversionHistory), PageRequest.of(0, 10), 1L
        );

        Mockito.doCallRealMethod().when(exchangeConversionHistoryResponseMapper).apply(conversionHistory);

        //When
        PagedResponse<ExchangeConversionHistoryResponse> response = responseMapper
                .toPagedResponse(conversionHistoryPage, exchangeConversionHistoryResponseMapper);

        //Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty().hasSize(1);
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getContentCount()).isEqualTo(1);
        assertThat(response.getCurrentPage()).isZero();
        assertThat(response.getPageSize()).isEqualTo(10);

    }
}