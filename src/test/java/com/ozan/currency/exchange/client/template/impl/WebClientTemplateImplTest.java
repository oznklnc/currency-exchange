package com.ozan.currency.exchange.client.template.impl;

import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.exception.ApiException;
import com.ozan.currency.exchange.exception.ClientException;
import com.ozan.currency.exchange.model.dto.WebClientRequestDto;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.enums.ErrorCode;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class WebClientTemplateImplTest extends UnitTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private WebClientTemplateImpl webClientTemplate;

    @Test
    void shouldReturnResponse_whenRequestIsSuccessful() {
        //Given
        String url = "https://api.example.com/data";
        FixerRateResponse expectedResponse = FixerRateResponse.builder()
                .base(Currency.EUR)
                .success(true)
                .rates(Map.of(Currency.USD, new BigDecimal("1.2")))
                .build();
        WebClientRequestDto<Void, FixerRateResponse> requestDto = WebClientRequestDto.<Void, FixerRateResponse>builder()
                .url(url)
                .responseClass(FixerRateResponse.class)
                .queryParams(Map.of("access_key", "accessKey"))
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq(url), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FixerRateResponse.class)).thenReturn(Mono.just(expectedResponse));

        //When
        FixerRateResponse actualResponse = webClientTemplate.get(requestDto);

        //Then
        assertThat(actualResponse).isNotNull().isEqualTo(expectedResponse);
        InOrder inOrder = inOrder(requestHeadersUriSpec, requestHeadersSpec, responseSpec);
        inOrder.verify(requestHeadersUriSpec).uri(eq(url), any(Function.class));
        inOrder.verify(requestHeadersSpec).accept(MediaType.APPLICATION_JSON);
        inOrder.verify(responseSpec).bodyToMono(FixerRateResponse.class);
    }


    @Test
    void shouldReturnFallbackExceptionWhenAnyExceptionOccurredFromWebClient() {
        //Given
        String url = "https://api.example.com/data";
        WebClientRequestDto<Void, FixerRateResponse> requestDto = WebClientRequestDto.<Void, FixerRateResponse>builder()
                .url(url)
                .responseClass(FixerRateResponse.class)
                .queryParams(Map.of("access_key", "accessKey"))
                .build();
        ClientException clientException = new ClientException("testException", ErrorCode.RETRY_GENERIC_ERROR);

        //When & Then
        assertThatThrownBy(() -> webClientTemplate.fallbackResponse(requestDto, clientException))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.RETRY_GENERIC_ERROR.getMessageCode());
    }

}