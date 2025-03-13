package com.ozan.currency.exchange.client.template.impl;

import com.ozan.currency.exchange.client.template.WebClientTemplate;
import com.ozan.currency.exchange.exception.ApiException;
import com.ozan.currency.exchange.exception.ClientException;
import com.ozan.currency.exchange.model.dto.WebClientRequestDto;
import com.ozan.currency.exchange.model.enums.ErrorCode;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebClientTemplateImpl implements WebClientTemplate {

    private final WebClient webClient;

    @Retry(name = "currencyExchangeRetry", fallbackMethod = "fallbackResponse")
    @Override
    public <C, R> R get(WebClientRequestDto<C, R> webClientRequestDto) {

        return webClient
                .get()
                .uri(webClientRequestDto.getUrl(), uriBuilder -> {
                    Optional.ofNullable(webClientRequestDto.getQueryParams())
                            .ifPresent(queryParams -> queryParams.forEach(uriBuilder::queryParam));
                    return uriBuilder.build();
                })
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorMessage ->
                                        Mono.error(new ClientException(errorMessage, ErrorCode.CLIENT_4XX_ERROR)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new ClientException(errorMessage, ErrorCode.CLIENT_5XX_ERROR)))
                )
                .bodyToMono(webClientRequestDto.getResponseClass())
                .block();

    }

    public <C, R> R fallbackResponse(WebClientRequestDto<C, R> webClientRequestDto, Exception e) {
        log.error("Fallback method called for url: {} errorMessage {}", webClientRequestDto.getUrl(), e.getMessage());
        throw new ApiException(ErrorCode.RETRY_GENERIC_ERROR);
    }


}
