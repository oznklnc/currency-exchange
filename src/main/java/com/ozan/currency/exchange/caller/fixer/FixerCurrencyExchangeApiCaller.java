package com.ozan.currency.exchange.caller.fixer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozan.currency.exchange.caller.CurrencyExchangeApiCaller;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerRateResponse;
import com.ozan.currency.exchange.caller.fixer.model.response.FixerSymbolResponse;
import com.ozan.currency.exchange.caller.fixer.properties.FixerCurrencyExchangeProperties;
import com.ozan.currency.exchange.client.template.WebClientTemplate;
import com.ozan.currency.exchange.constant.CacheConstants;
import com.ozan.currency.exchange.exception.ClientException;
import com.ozan.currency.exchange.model.dto.WebClientRequestDto;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.model.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "fixer-currency-exchange.mock.enabled", havingValue = "false", matchIfMissing = true)
public class FixerCurrencyExchangeApiCaller implements CurrencyExchangeApiCaller {

    private final WebClientTemplate webClientTemplate;
    private final FixerCurrencyExchangeProperties fixerCurrencyExchangeProperties;
    private final ObjectMapper objectMapper;

    @Override
    public FixerSymbolResponse getExchangeSymbols() {
        WebClientRequestDto<Void, FixerSymbolResponse> webClientRequestDto = buildSymbolRequest();
        FixerSymbolResponse fixerSymbolResponse = webClientTemplate.get(webClientRequestDto);
        if (!fixerSymbolResponse.getSuccess()) {
            throw new ClientException(buildErrorMessage(fixerSymbolResponse.getError()), ErrorCode.FIXER_GENERIC_ERROR);
        }
        return fixerSymbolResponse;
    }

    @Override
    @Cacheable(value = CacheConstants.FIXER_CACHE_NAME, key = "#base.getCode() + '-' + #target.getCode()")
    public FixerRateResponse getExchangeRates(Currency base, Currency target) {
        WebClientRequestDto<Void, FixerRateResponse> webClientRequestDto = buildRateRequest(base, target);
        FixerRateResponse fixerRateResponse = webClientTemplate.get(webClientRequestDto);
        if (!fixerRateResponse.getSuccess()) {
            throw new ClientException(buildErrorMessage(fixerRateResponse.getError()), ErrorCode.FIXER_GENERIC_ERROR);
        }
        return fixerRateResponse;
    }

    private WebClientRequestDto<Void, FixerSymbolResponse> buildSymbolRequest() {
        return WebClientRequestDto.<Void, FixerSymbolResponse>builder()
                .url(fixerCurrencyExchangeProperties.getBaseUrl() + fixerCurrencyExchangeProperties.getSymbolUrl())
                .queryParams(
                        Map.of("access_key", fixerCurrencyExchangeProperties.getAccessKey())
                )
                .responseClass(FixerSymbolResponse.class)
                .build();
    }

    private WebClientRequestDto<Void, FixerRateResponse> buildRateRequest(Currency base, Currency target) {

        return WebClientRequestDto.<Void, FixerRateResponse>builder()
                .url(fixerCurrencyExchangeProperties.getBaseUrl() + fixerCurrencyExchangeProperties.getRatesUrl())
                .queryParams(
                        Map.of(
                                "access_key", fixerCurrencyExchangeProperties.getAccessKey(),
                                "base", base.getCode(),
                                "symbols", target.getCode()
                        )
                )
                .responseClass(FixerRateResponse.class)
                .build();

    }

    private String buildErrorMessage(Map<String, Object> error) {
        try {
            return objectMapper.writeValueAsString(error);
        } catch (JsonProcessingException e) {
            log.error("Error while converting error map to string", e);
            return "";
        }
    }
}
