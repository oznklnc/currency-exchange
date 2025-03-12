package com.ozan.currency.exchange.entity;

import com.ozan.currency.exchange.model.enums.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "conversion_history")
public class ConversionHistory extends BaseEntity {

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "source_currency")
    @Enumerated(EnumType.STRING)
    private Currency sourceCurrency;

    @Column(name = "target_currency")
    @Enumerated(EnumType.STRING)
    private Currency targetCurrency;

    @Column(name = "amount", precision = 30, scale = 8)
    private BigDecimal amount;

    @Column(name = "converted_amount", precision = 30, scale = 8)
    private BigDecimal convertedAmount;

}
