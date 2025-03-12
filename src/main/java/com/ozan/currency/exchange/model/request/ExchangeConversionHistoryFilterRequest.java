package com.ozan.currency.exchange.model.request;

import com.ozan.currency.exchange.model.Base;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeConversionHistoryRequest extends Base {

    private String transactionId;

    private LocalDate transactionDate;

    @Builder.Default
    private Integer pageNumber = 0;

    @Builder.Default
    private Integer pageSize = 5;


    public Pageable getPageable() {
        return PageRequest.of(pageNumber, pageSize);
    }

    public LocalDateTime getStartOfTransactionDate() {
        return transactionDate.atStartOfDay();
    }
    public LocalDateTime getEndOfTransactionDate() {
        return transactionDate.atStartOfDay().plusDays(1).minusNanos(1);
    }
}
