package com.ozan.currency.exchange.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ozan.currency.exchange.base.IntegrationBaseTest;
import com.ozan.currency.exchange.constant.UrlPath;
import com.ozan.currency.exchange.model.dto.ApiErrorDto;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.request.ConversionRequest;
import com.ozan.currency.exchange.model.response.*;
import com.ozan.currency.exchange.repository.ConversionHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExchangeControllerTest extends IntegrationBaseTest {

    @Autowired
    private ConversionHistoryRepository conversionHistoryRepository;

    @Test
    void shouldGetAllCurrencies() throws Exception {
        //When
        MvcResult mvcResult = mockMvc.perform(get(UrlPath.BASE_PATH + "/currencies")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        ExchangeCurrencyListResponse exchangeCurrencyListResponse = objectMapper.readValue(response.getContentAsString(), ExchangeCurrencyListResponse.class);

        //Then
        assertThat(exchangeCurrencyListResponse).isNotNull();
        assertThat(exchangeCurrencyListResponse.getCurrencies()).isNotEmpty();
        assertThat(exchangeCurrencyListResponse.getCurrencies()).hasSize(3);

        assertThat(exchangeCurrencyListResponse.getCurrencies().get(0).getCurrencyCode()).isEqualTo("EUR");
        assertThat(exchangeCurrencyListResponse.getCurrencies().get(0).getDefinition()).isEqualTo("Euro");

        assertThat(exchangeCurrencyListResponse.getCurrencies().get(1).getCurrencyCode()).isEqualTo("TRY");
        assertThat(exchangeCurrencyListResponse.getCurrencies().get(1).getDefinition()).isEqualTo("Turkish Lira");

        assertThat(exchangeCurrencyListResponse.getCurrencies().get(2).getCurrencyCode()).isEqualTo("USD");
        assertThat(exchangeCurrencyListResponse.getCurrencies().get(2).getDefinition()).isEqualTo("United States Dollar");
    }

    @Test
    void shouldReturnExchangeRateResponseWhenGetExchangeRate() throws Exception {
        //When
        MvcResult mvcResult = mockMvc.perform(get(UrlPath.BASE_PATH + "/exchange-rate")
                        .param("from", "USD")
                        .param("to", "TRY")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        ExchangeRateResponse exchangeRateResponse = objectMapper.readValue(response.getContentAsString(), ExchangeRateResponse.class);

        //Then
        assertThat(exchangeRateResponse).isNotNull();
        assertThat(exchangeRateResponse.getFrom()).isEqualTo(Currency.USD);
        assertThat(exchangeRateResponse.getTo()).isEqualTo(Currency.TRY);
        assertThat(exchangeRateResponse.getRate()).isNotNull();
    }

    @Test
    void shouldReturnBadRequestWhenGetExchangeRate() throws Exception {
        //When
        MvcResult mvcResult = mockMvc.perform(get(UrlPath.BASE_PATH + "/exchange-rate")
                        .param("to", "TRY")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        ApiErrorDto errorResponse = objectMapper.readValue(response.getContentAsString(), ApiErrorDto.class);

        //Then
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrors()).isNotEmpty().hasSize(1);
        assertThat(errorResponse.getUrl()).isNotNull();
    }

    @Test
    void shouldConvertCurrency() throws Exception {
        //Given
        ConversionRequest request = ConversionRequest.builder()
                .from("USD")
                .to("TRY")
                .amount(new BigDecimal("10.00"))
                .build();
        //When
        MvcResult mvcResult = mockMvc.perform(post(UrlPath.BASE_PATH + "/convert")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        ExchangeConversionResponse exchangeConversionResponse = objectMapper.readValue(response.getContentAsString(), ExchangeConversionResponse.class);

        //Then
        assertThat(exchangeConversionResponse).isNotNull();
        assertThat(exchangeConversionResponse.getFrom()).isEqualTo(Currency.USD);
        assertThat(exchangeConversionResponse.getTo()).isEqualTo(Currency.TRY);
        assertThat(exchangeConversionResponse.getAmount()).isNotNull();
        assertThat(exchangeConversionResponse.getConvertedAmount()).isNotNull();

        boolean result = conversionHistoryRepository.existsBySourceCurrencyAndTargetCurrency(request.getFrom(), request.getTo());
        assertThat(result).isTrue();
    }

    @Test
    @Sql(scripts = {"/sql/test_schema.sql", "/sql/insert_conversion_history.sql"})
    void shouldReturnGetConversionHistory() throws Exception {
        //When
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("transactionDate", LocalDate.now().format(formatter));
        params.add("pageSize", "3");

        MvcResult mvcResult = mockMvc.perform(get(UrlPath.BASE_PATH + "/conversion-history")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        PagedResponse<ExchangeConversionHistoryResponse> exchangeConversionHistoryResponse = objectMapper.readValue(
                response.getContentAsString(),
                new TypeReference<>() {}
        );
        assertThat(exchangeConversionHistoryResponse).isNotNull();
        assertThat(exchangeConversionHistoryResponse.getContent()).isNotEmpty();
        assertThat(exchangeConversionHistoryResponse.getTotalPages()).isEqualTo(3);
        assertThat(exchangeConversionHistoryResponse.getTotalElements()).isEqualTo(8);
        assertThat(exchangeConversionHistoryResponse.getContentCount()).isEqualTo(3);
        assertThat(exchangeConversionHistoryResponse.getPageSize()).isEqualTo(3);
    }
}