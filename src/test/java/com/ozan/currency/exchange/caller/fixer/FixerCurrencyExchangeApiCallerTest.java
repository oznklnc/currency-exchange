package com.ozan.currency.exchange.caller.fixer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerSymbolResponse;
import com.ozan.currency.exchange.caller.fixer.properties.FixerCurrencyExchangeProperties;
import com.ozan.currency.exchange.client.template.WebClientTemplate;
import com.ozan.currency.exchange.exception.ClientException;
import com.ozan.currency.exchange.model.dto.WebClientRequestDto;
import com.ozan.currency.exchange.model.enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FixerCurrencyExchangeApiCallerTest extends UnitTest {

    @Mock
    private WebClientTemplate webClientTemplate;

    @Mock
    private FixerCurrencyExchangeProperties properties;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private FixerCurrencyExchangeApiCaller apiCaller;

    private static final String BASE_URL = "https://example.com";
    private static final String ACCESS_KEY = "accessKey";
    private static final String SYMBOL_URL = "/symbols";
    private static final String RATES_URL = "/rates";

    @Captor
    private ArgumentCaptor<WebClientRequestDto<Void, FixerSymbolResponse>> symbolRequestCaptor;
    @Captor
    private ArgumentCaptor<WebClientRequestDto<Void, FixerRateResponse>> rateRequestCaptor;

    @BeforeEach
    void setUp() {
        when(properties.getBaseUrl()).thenReturn(BASE_URL);
        when(properties.getAccessKey()).thenReturn(ACCESS_KEY);
    }

    @Test
    void shouldReturnExchangeResponse() throws Exception {
        //given
        FixerSymbolResponse response = FixerSymbolResponse.builder()
                .success(true)
                .symbols(Map.of(Currency.USD, "United States Dollar", Currency.EUR, "Euro"))
                .build();

        when(properties.getSymbolUrl()).thenReturn(SYMBOL_URL);
        when(webClientTemplate.get(any())).thenReturn(response);

        //when
        FixerSymbolResponse result = apiCaller.getExchangeSymbols();

        //then
        assertThat(result).isEqualTo(response);
        InOrder inOrder = inOrder(webClientTemplate, objectMapper);
        inOrder.verify(webClientTemplate).get(symbolRequestCaptor.capture());
        inOrder.verify(objectMapper, never()).writeValueAsString(any(FixerSymbolResponse.class));

        WebClientRequestDto<Void, FixerSymbolResponse> capturedRequest = symbolRequestCaptor.getValue();
        assertThat(capturedRequest.getUrl()).isEqualTo(BASE_URL + SYMBOL_URL);
        assertThat(capturedRequest.getQueryParams()).containsExactly(Map.entry("access_key", ACCESS_KEY));
        assertThat(capturedRequest.getResponseClass()).isEqualTo(FixerSymbolResponse.class);
        assertThat(capturedRequest.getRequestBody()).isNull();
    }

    @Test
    void shouldThrowExceptionWhenFixerSymbolResponseIsNotSuccess() throws Exception {
        //given
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code", 101);
        error.put("info", "Invalid access key");
        FixerSymbolResponse response = FixerSymbolResponse.builder()
                .success(false)
                .error(error)
                .build();

        when(properties.getSymbolUrl()).thenReturn(SYMBOL_URL);
        when(webClientTemplate.get(any())).thenReturn(response);

        //when & then
        assertThatThrownBy(() -> apiCaller.getExchangeSymbols())
                .isInstanceOf(ClientException.class)
                .hasMessage("{\"code\":101,\"info\":\"Invalid access key\"}");
        InOrder inOrder = inOrder(webClientTemplate, objectMapper);
        inOrder.verify(webClientTemplate).get(symbolRequestCaptor.capture());
        inOrder.verify(objectMapper).writeValueAsString(any(Map.class));
    }

    @Test
    void shouldReturnFixerRateResponse() throws Exception {
        //given
        FixerRateResponse response = FixerRateResponse.builder()
                .success(true)
                .base(Currency.EUR)
                .rates(Map.of(Currency.USD, new BigDecimal("0.1")))
                .build();

        when(properties.getRatesUrl()).thenReturn(RATES_URL);
        when(webClientTemplate.get(any())).thenReturn(response);

        //when
        FixerRateResponse result = apiCaller.getExchangeRates(Currency.EUR, Currency.USD);

        //then
        assertThat(result).isEqualTo(response);
        InOrder inOrder = inOrder(webClientTemplate, objectMapper);
        inOrder.verify(webClientTemplate).get(rateRequestCaptor.capture());
        inOrder.verify(objectMapper, never()).writeValueAsString(any(FixerRateResponse.class));

        WebClientRequestDto<Void, FixerRateResponse> capturedRequest = rateRequestCaptor.getValue();
        assertThat(capturedRequest.getUrl()).isEqualTo(BASE_URL + RATES_URL);
        assertThat(capturedRequest.getQueryParams()).containsAnyOf(
                Map.entry("access_key", ACCESS_KEY),
                Map.entry("base", Currency.USD.getCode()),
                Map.entry("symbols", Currency.EUR.getCode())
        );
    }

    @Test
    void shouldThrowExceptionWhenFixerRateResponseIsNotSuccess() throws Exception {
        //given
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code", 101);
        error.put("info", "Invalid access key");
        FixerRateResponse response = FixerRateResponse.builder()
                .success(false)
                .error(error)
                .build();

        when(properties.getRatesUrl()).thenReturn(RATES_URL);
        when(webClientTemplate.get(any())).thenReturn(response);
        when(objectMapper.writeValueAsString(any(Map.class))).thenThrow(JsonProcessingException.class);

        //when & then
        assertThatThrownBy(() -> apiCaller.getExchangeRates(Currency.EUR, Currency.USD))
                .isInstanceOf(ClientException.class)
                .hasMessage("");
        InOrder inOrder = inOrder(webClientTemplate, objectMapper);
        inOrder.verify(webClientTemplate).get(rateRequestCaptor.capture());
        inOrder.verify(objectMapper).writeValueAsString(any(Map.class));
    }

    @Test
    void shouldReturnFixerRateResponseWithTargets() throws Exception {
        //given
        List<Currency> targets = List.of(Currency.USD, Currency.TRY);
        FixerRateResponse response = FixerRateResponse.builder()
                .success(true)
                .base(Currency.EUR)
                .rates(Map.of(Currency.USD, new BigDecimal("0.1"), Currency.TRY, new BigDecimal("0.2")))
                .build();

        when(properties.getRatesUrl()).thenReturn(RATES_URL);
        when(webClientTemplate.get(any())).thenReturn(response);

        //when
        FixerRateResponse result = apiCaller.getExchangeRatesWithTargets(Currency.EUR, targets);

        //then
        assertThat(result).isEqualTo(response);
        InOrder inOrder = inOrder(webClientTemplate, objectMapper);
        inOrder.verify(webClientTemplate).get(rateRequestCaptor.capture());
        inOrder.verify(objectMapper, never()).writeValueAsString(any(FixerRateResponse.class));

        WebClientRequestDto<Void, FixerRateResponse> capturedRequest = rateRequestCaptor.getValue();
        assertThat(capturedRequest.getUrl()).isEqualTo(BASE_URL + RATES_URL);
        assertThat(capturedRequest.getQueryParams()).containsAnyOf(
                Map.entry("access_key", ACCESS_KEY),
                Map.entry("base", Currency.USD.getCode()),
                Map.entry("symbols", targets.stream().map(Currency::getCode).collect(Collectors.joining(",")))
        );
    }

    @Test
    void shouldThrowExceptionWhenFixerRateResponseWithTargetsIsNotSuccess() throws Exception {
        //given
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code", 101);
        error.put("info", "Invalid access key");
        FixerRateResponse response = FixerRateResponse.builder()
                .success(false)
                .error(error)
                .build();

        when(properties.getRatesUrl()).thenReturn(RATES_URL);
        when(webClientTemplate.get(any())).thenReturn(response);
        when(objectMapper.writeValueAsString(any(Map.class))).thenThrow(JsonProcessingException.class);

        //when & then
        assertThatThrownBy(() -> apiCaller.getExchangeRatesWithTargets(Currency.EUR, List.of(Currency.USD)))
                .isInstanceOf(ClientException.class)
                .hasMessage("");
        InOrder inOrder = inOrder(webClientTemplate, objectMapper);
        inOrder.verify(webClientTemplate).get(rateRequestCaptor.capture());
        inOrder.verify(objectMapper).writeValueAsString(any(Map.class));
    }
}