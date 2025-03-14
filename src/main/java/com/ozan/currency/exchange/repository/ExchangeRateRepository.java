package com.ozan.currency.exchange.repository;

import com.ozan.currency.exchange.entity.ExchangeRate;
import com.ozan.currency.exchange.model.enums.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findBySourceCurrencyAndTargetCurrency(Currency sourceCurrency, Currency targetCurrency);

    Page<ExchangeRate> findAllByValidDateBefore(LocalDateTime validDate, Pageable pageable);

}
