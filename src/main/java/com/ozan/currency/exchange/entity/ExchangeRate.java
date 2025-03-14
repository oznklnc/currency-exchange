package com.ozan.currency.exchange.entity;

import com.ozan.currency.exchange.model.enums.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "exchange_rate")
public class ExchangeRate extends BaseEntity {

    @Column(name = "source_currency")
    @Enumerated(EnumType.STRING)
    private Currency sourceCurrency;

    @Column(name = "target_currency")
    @Enumerated(EnumType.STRING)
    private Currency targetCurrency;

    @Column(name = "rate", precision = 18, scale = 7)
    private BigDecimal rate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "valid_date")
    private LocalDateTime validDate;


}
