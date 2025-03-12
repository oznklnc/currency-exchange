package com.ozan.currency.exchange.configuration.properties;

import com.ozan.currency.exchange.model.dto.CacheSpecDto;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@Data
@ConfigurationProperties(prefix = "currency.exchange.cache")
public class CacheSpecProperties {

    private Map<String, CacheSpecDto> specs;
}
