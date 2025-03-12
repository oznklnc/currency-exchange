package com.ozan.currency.exchange.caller.fixer.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ozan.currency.exchange.caller.fixer.model.Base;
import com.ozan.currency.exchange.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FixerRateResponse extends Base {

    private Long timestamp;
    private Currency base;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Map<Currency, BigDecimal> rates;

}
