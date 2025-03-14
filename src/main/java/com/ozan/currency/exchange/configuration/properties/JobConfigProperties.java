package com.ozan.currency.exchange.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "job.config")
public class JobConfigProperties {

    private String updaterName;
    private Long validityPeriod;
    private Integer fetchLimit;
    private Integer pageNumber;

}
