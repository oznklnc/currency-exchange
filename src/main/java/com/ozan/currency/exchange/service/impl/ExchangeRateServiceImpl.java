package com.ozan.currency.exchange.service.impl;

import com.ozan.currency.exchange.caller.CurrencyExchangeApiCaller;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerSymbolResponse;
import com.ozan.currency.exchange.configuration.properties.JobConfigProperties;
import com.ozan.currency.exchange.entity.ExchangeRate;
import com.ozan.currency.exchange.mapper.ExchangeCurrencyListResponseMapper;
import com.ozan.currency.exchange.mapper.ExchangeRateResponseMapper;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeCurrencyListResponse;
import com.ozan.currency.exchange.model.response.ExchangeRateResponse;
import com.ozan.currency.exchange.repository.ExchangeRateRepository;
import com.ozan.currency.exchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final CurrencyExchangeApiCaller currencyExchangeApiCaller;
    private final ExchangeCurrencyListResponseMapper exchangeCurrencyListResponseMapper;
    private final ExchangeRateResponseMapper exchangeRateResponseMapper;
    private final ExchangeRateRepository exchangeRateRepository;
    private final JobConfigProperties jobConfigProperties;

    @Override
    public ExchangeCurrencyListResponse getAllCurrencies() {
        FixerSymbolResponse exchangeSymbols = currencyExchangeApiCaller.getExchangeSymbols();
        return exchangeCurrencyListResponseMapper.apply(exchangeSymbols);
    }

    @Override
    public ExchangeRateResponse getExchangeRate(Currency from, Currency to) {
        LocalDateTime now = LocalDateTime.now();
        Optional<ExchangeRate> exchangeRateOpt = exchangeRateRepository.findBySourceCurrencyAndTargetCurrency(from, to);
        if (exchangeRateOpt.isPresent() && exchangeRateOpt.get().getValidDate().isAfter(now)) {
            return exchangeRateResponseMapper.apply(exchangeRateOpt.get());
        }
        FixerRateResponse exchangeRates = currencyExchangeApiCaller.getExchangeRates(from, to);
        ExchangeRate exchangeRate = exchangeRateOpt
                .map(rate -> updateExchangeRate(rate, exchangeRates.getRates(), now))
                .orElseGet(() -> buildExchangeRate(exchangeRates, to, now));

        return exchangeRateResponseMapper.apply(exchangeRateRepository.save(exchangeRate));
    }

    @Override
    public List<ExchangeRate> getExchangeRateListToBeUpdated() {
        LocalDateTime now = LocalDateTime.now();
        Page<ExchangeRate> exchangeRatePage = exchangeRateRepository.findAllByValidDateBefore(now, PageRequest.of(jobConfigProperties.getPageNumber(), jobConfigProperties.getFetchLimit(), Sort.by(Sort.Order.asc("sourceCurrency"))));
        return exchangeRatePage.getContent();
    }

    @Override
    public FixerRateResponse getFixerRates(Currency from, List<Currency> targets) {
        return currencyExchangeApiCaller.getExchangeRatesWithTargets(from, targets);
    }

    @Transactional
    @Override
    public void updateExchangeRates(Map<Currency, BigDecimal> rates, List<ExchangeRate> exchangeRates) {
        LocalDateTime now = LocalDateTime.now();
        exchangeRates.forEach(rate -> {
            updateExchangeRate(rate, rates, now);
            rate.setUpdatedBy(jobConfigProperties.getUpdaterName());
        });
        exchangeRateRepository.saveAll(exchangeRates);
    }

    private ExchangeRate updateExchangeRate(ExchangeRate rate, Map<Currency, BigDecimal> exchangeRates, LocalDateTime now) {
        Currency targetCurrency = rate.getTargetCurrency();
        rate.setRate(exchangeRates.get(targetCurrency));
        rate.setValidDate(now.plusMinutes(jobConfigProperties.getValidityPeriod()));
        return rate;
    }

    private ExchangeRate buildExchangeRate(FixerRateResponse exchangeRates, Currency to, LocalDateTime now) {
        return ExchangeRate.builder()
                .sourceCurrency(exchangeRates.getBase())
                .targetCurrency(to)
                .rate(exchangeRates.getRates().get(to))
                .validDate(now.plusMinutes(jobConfigProperties.getValidityPeriod()))
                .build();
    }
}
