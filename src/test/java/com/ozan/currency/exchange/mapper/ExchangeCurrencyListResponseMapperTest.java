package com.ozan.currency.exchange.mapper;

import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerSymbolResponse;
import com.ozan.currency.exchange.model.dto.CurrencyDto;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.response.ExchangeCurrencyListResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExchangeCurrencyListResponseMapperTest extends UnitTest {

    @InjectMocks
    private ExchangeCurrencyListResponseMapper exchangeCurrencyListResponseMapper;

    @Test
    void shouldReturnExchangeCurrencyListResponse() {
        //Given
        FixerSymbolResponse fixerSymbolResponse = FixerSymbolResponse.builder()
                .symbols(Map.of(Currency.EUR, "Euro", Currency.USD, "Dollar"))
                .build();

        //When
        ExchangeCurrencyListResponse response = exchangeCurrencyListResponseMapper.apply(fixerSymbolResponse);

        //Then
        assertThat(response).isNotNull();
        assertThat(response.getCurrencies()).isNotEmpty();
        assertThat(response.getCurrencies()).hasSize(fixerSymbolResponse.getSymbols().size());
        assertThat(response.getCurrencies()).containsExactly(
                CurrencyDto.builder().currencyCode(Currency.EUR.getCode()).definition("Euro").build(),
                CurrencyDto.builder().currencyCode(Currency.USD.getCode()).definition("Dollar").build()
        );

    }
}