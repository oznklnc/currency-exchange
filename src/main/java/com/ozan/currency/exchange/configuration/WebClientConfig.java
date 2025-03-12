package com.ozan.currency.exchange.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("")
                .filter(logRequestResponse())
                .build();
    }

    public ExchangeFilterFunction logRequestResponse() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            logRequest(request);
            return Mono.just(request);
        }).andThen(ExchangeFilterFunction.ofResponseProcessor(this::logResponse));
    }

    private void logRequest(ClientRequest request) {
        log.info("Request: \nMethod: {}\nURL: {}\nHeaders: {}",
                request.method(),
                request.url(),
                formatHeaders(request.headers()));

    }

    private Mono<ClientResponse> logResponse(ClientResponse response) {
        HttpStatusCode httpStatusCode = response.statusCode();
        HttpHeaders headers = response.headers().asHttpHeaders();
        log.info("Response: \nStatus: {}\nHeaders: {}", httpStatusCode, formatHeaders(headers));
        return response.bodyToMono(String.class)
                .doOnNext(body -> log.info("Response Body: {}", body))
                .map(body -> ClientResponse
                        .create(response.statusCode())
                        .headers(headersConsumer -> headersConsumer.addAll(headers))
                        .body(body)
                        .build());
    }

    private String formatHeaders(HttpHeaders headers) {
        StringBuilder formattedHeaders = new StringBuilder();
        headers.forEach((name, values) -> formattedHeaders.append(name)
                .append(": ")
                .append(values)
                .append("\n")
        );
        return formattedHeaders.toString();
    }

}
