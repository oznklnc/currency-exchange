package com.ozan.currency.exchange.job;

import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.entity.ExchangeRate;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.config.enabled", havingValue = "true")
public class ExchangeRateJobService {

    private final ExchangeRateService exchangeRateService;

    @Scheduled(fixedRateString = "${job.config.fixed-rate}")
    public void updateExchangeRatesTask() {
        log.info("Updating exchange rates...");
        List<ExchangeRate> rateListToBeUpdated = exchangeRateService.getExchangeRateListToBeUpdated();
        if (rateListToBeUpdated.isEmpty()) {
            log.info("No exchange rates to be updated.");
            return;
        }

        log.info("Found {} exchange rates to be updated.", rateListToBeUpdated.size());

        Map<Currency, List<ExchangeRate>> groupedExchangeRates = rateListToBeUpdated.stream()
                .collect(Collectors.groupingBy(ExchangeRate::getSourceCurrency));

        log.info("Grouped exchange rates by source currency: {}", groupedExchangeRates.keySet());

        groupedExchangeRates.forEach((sourceCurrency, exchangeRates) -> {
            try {
                Map<Currency, BigDecimal> fixerRates = retrieveFixerRateResponse(sourceCurrency, exchangeRates);
                exchangeRateService.updateExchangeRates(fixerRates, exchangeRates);
            } catch (Exception e) {
                log.error("Error occurred while updating exchange rates for source currency: {}", sourceCurrency, e);
            }
        });

        log.info("Exchange rates updated.");
    }


    private Map<Currency, BigDecimal> retrieveFixerRateResponse(Currency sourceCurrency, List<ExchangeRate> exchangeRates) {

        List<Currency> targetCurrencies = exchangeRates.stream()
                .map(ExchangeRate::getTargetCurrency)
                .collect(Collectors.toList());
        FixerRateResponse response = exchangeRateService.getFixerRates(sourceCurrency, targetCurrencies);
        return response.getRates();


    }
}
