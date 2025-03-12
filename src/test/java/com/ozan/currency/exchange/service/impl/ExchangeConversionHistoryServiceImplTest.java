package com.ozan.currency.exchange.service.impl;

import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.entity.ConversionHistory;
import com.ozan.currency.exchange.mapper.ExchangeConversionHistoryResponseMapper;
import com.ozan.currency.exchange.mapper.ResponseMapper;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.request.ExchangeConversionHistoryFilterRequest;
import com.ozan.currency.exchange.model.response.ExchangeConversionHistoryResponse;
import com.ozan.currency.exchange.model.response.PagedResponse;
import com.ozan.currency.exchange.repository.ConversionHistoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class ExchangeConversionHistoryServiceImplTest extends UnitTest {

    @Mock
    private ConversionHistoryRepository conversionHistoryRepository;
    @Mock
    private ResponseMapper responseMapper;
    @Mock
    private ExchangeConversionHistoryResponseMapper exchangeConversionHistoryResponseMapper;
    @InjectMocks
    private ExchangeConversionHistoryServiceImpl exchangeConversionHistoryService;

    @Test
    void shouldReturnExchangeConversionHistory() {
        //Given
        ExchangeConversionHistoryFilterRequest request = ExchangeConversionHistoryFilterRequest.builder()
                .transactionDate(LocalDate.now())
                .build();
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
                List.of(conversionHistory), PageRequest.of(0, 5), 1L
        );
        PagedResponse<ExchangeConversionHistoryResponse> expectedResponse = new PagedResponse<>();
        when(conversionHistoryRepository.findByTransactionIdAndDate(
                request.getTransactionId(),
                request.getStartOfTransactionDate(),
                request.getEndOfTransactionDate(),
                PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.by(Sort.Order.asc("createdAt")))
        )).thenReturn(conversionHistoryPage);
        when(responseMapper.toPagedResponse(conversionHistoryPage, exchangeConversionHistoryResponseMapper)).thenReturn(expectedResponse);

        //When
        PagedResponse<ExchangeConversionHistoryResponse> actualResponse = exchangeConversionHistoryService.getExchangeConversionHistory(request);

        //Then
        assertThat(actualResponse).isEqualTo(expectedResponse);
        InOrder inOrder = inOrder(conversionHistoryRepository, responseMapper);
        inOrder.verify(conversionHistoryRepository).findByTransactionIdAndDate(
                request.getTransactionId(),
                request.getStartOfTransactionDate(),
                request.getEndOfTransactionDate(),
                PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.by(Sort.Order.asc("createdAt")))
        );
        inOrder.verify(responseMapper).toPagedResponse(conversionHistoryPage, exchangeConversionHistoryResponseMapper);
    }
}