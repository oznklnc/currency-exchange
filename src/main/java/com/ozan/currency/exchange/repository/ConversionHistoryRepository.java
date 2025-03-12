package com.ozan.currency.exchange.repository;

import com.ozan.currency.exchange.entity.ConversionHistory;
import com.ozan.currency.exchange.model.enums.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConversionHistoryRepository extends JpaRepository<ConversionHistory, Long> {


    @Query("SELECT c FROM ConversionHistory c WHERE " +
            "(COALESCE(:transactionId, '') = '' OR c.transactionId = :transactionId) AND " +
            "(:startOfDay IS NULL OR :endOfDay IS NULL OR c.createdAt BETWEEN :startOfDay AND :endOfDay)")
    Page<ConversionHistory> findByTransactionIdAndDate(
            @Param("transactionId") String transactionId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            Pageable pageable);

    boolean existsBySourceCurrencyAndTargetCurrency(Currency sourceCurrency, Currency targetCurrency);


}
