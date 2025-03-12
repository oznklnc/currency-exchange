package com.ozan.currency.exchange.service.impl;

import com.ozan.currency.exchange.entity.ConversionHistory;
import com.ozan.currency.exchange.mapper.ExchangeConversionHistoryResponseMapper;
import com.ozan.currency.exchange.mapper.ResponseMapper;
import com.ozan.currency.exchange.model.request.ExchangeConversionHistoryFilterRequest;
import com.ozan.currency.exchange.model.response.ExchangeConversionHistoryResponse;
import com.ozan.currency.exchange.model.response.PagedResponse;
import com.ozan.currency.exchange.repository.ConversionHistoryRepository;
import com.ozan.currency.exchange.service.ExchangeConversionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeConversionHistoryServiceImpl implements ExchangeConversionHistoryService {

    private final ConversionHistoryRepository conversionHistoryRepository;
    private final ResponseMapper responseMapper;
    private final ExchangeConversionHistoryResponseMapper exchangeConversionHistoryResponseMapper;

    @Override
    public PagedResponse<ExchangeConversionHistoryResponse> getExchangeConversionHistory(ExchangeConversionHistoryFilterRequest request) {
        Page<ConversionHistory> conversionHistoryPage = conversionHistoryRepository.findByTransactionIdAndDate(
                request.getTransactionId(),
                request.getStartOfTransactionDate(),
                request.getEndOfTransactionDate(),
                PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.by(Sort.Order.asc("createdAt")))
        );
        return responseMapper
                .toPagedResponse(conversionHistoryPage, exchangeConversionHistoryResponseMapper);
    }
}
