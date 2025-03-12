package com.ozan.currency.exchange.caller.fixer.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "fixer-currency-exchange")
public class FixerCurrencyExchangeProperties {

    private String baseUrl;
    private String accessKey;
    private String symbolUrl;
    private String ratesUrl;
}
