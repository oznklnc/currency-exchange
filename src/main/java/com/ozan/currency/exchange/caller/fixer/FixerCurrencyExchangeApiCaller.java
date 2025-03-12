package com.ozan.currency.exchange.caller.currency.exchange.fixer;

import com.ozan.currency.exchange.caller.currency.exchange.CurrencyExchangeApiCaller;
import com.ozan.currency.exchange.caller.currency.exchange.fixer.model.response.SymbolResponse;
import com.ozan.currency.exchange.caller.currency.exchange.fixer.properties.FixerCurrencyExchangeProperties;
import com.ozan.currency.exchange.client.template.WebClientTemplate;
import com.ozan.currency.exchange.model.dto.CurrencyDto;
import com.ozan.currency.exchange.model.dto.WebClientRequestDto;
import com.ozan.currency.exchange.model.response.CurrencyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FixerCurrencyExchangeApiCaller implements CurrencyExchangeApiCaller {

    private final WebClientTemplate webClientTemplate;
    private final FixerCurrencyExchangeProperties fixerCurrencyExchangeProperties;

    @Override
    public CurrencyResponse getExchangeSymbols() {
        WebClientRequestDto<Void, SymbolResponse> webClientRequestDto = buildWebClientRequestDto();
        SymbolResponse symbolResponse = webClientTemplate.get(webClientRequestDto);
        List<CurrencyDto> currencies = symbolResponse.getSymbols().entrySet()
                .stream()
                .map(entry -> CurrencyDto.builder()
                        .code(entry.getKey())
                        .definition(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        return CurrencyResponse.builder()
                .success(symbolResponse.getSuccess())
                .currencies(currencies)
                .build();
    }

    private WebClientRequestDto<Void, SymbolResponse> buildWebClientRequestDto() {
        return WebClientRequestDto.<Void, SymbolResponse>builder()
                .url(fixerCurrencyExchangeProperties.getBaseUrl() + fixerCurrencyExchangeProperties.getSymbolUrl())
                .queryParams(
                        Map.of("access_key", fixerCurrencyExchangeProperties.getAccessKey())
                )
                .responseClass(SymbolResponse.class)
                .build();
    }
}
