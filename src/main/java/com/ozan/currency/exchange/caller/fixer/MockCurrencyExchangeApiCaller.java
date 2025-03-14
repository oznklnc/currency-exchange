package com.ozan.currency.exchange.caller.fixer;

import com.ozan.currency.exchange.caller.CurrencyExchangeApiCaller;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerSymbolResponse;
import com.ozan.currency.exchange.model.enums.Currency;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "fixer-currency-exchange.mock.enabled", havingValue = "true")
public class MockCurrencyExchangeApiCaller implements CurrencyExchangeApiCaller {

    @Override
    public FixerSymbolResponse getExchangeSymbols() {
        return FixerSymbolResponse.builder()
                .success(true)
                .symbols(
                        Map.of(
                                Currency.USD, "United States Dollar",
                                Currency.EUR, "Euro",
                                Currency.TRY, "Turkish Lira"
                        )
                )
                .build();
    }

    @Override
    public FixerRateResponse getExchangeRates(Currency base, Currency target) {
        return FixerRateResponse.builder()
                .success(true)
                .timestamp(1620000000L)
                .base(base)
                .rates(
                        Map.of(
                                target, new BigDecimal("39.652626")
                        )
                )
                .build();
    }

    @Override
    public FixerRateResponse getExchangeRatesWithTargets(Currency base, List<Currency> targets) {
        return FixerRateResponse.builder()
                .success(true)
                .timestamp(1620000000L)
                .base(base)
                .rates(
                        targets.stream()
                                .collect(
                                        Collectors.toMap(
                                                target -> target,
                                                target -> new BigDecimal("1.652626")
                                        )
                                )
                )
                .build();
    }
}
