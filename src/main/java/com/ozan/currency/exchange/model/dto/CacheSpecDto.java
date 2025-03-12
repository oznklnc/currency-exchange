package com.ozan.currency.exchange.model.dto;

import lombok.Data;

import java.time.Duration;

@Data
public class CacheSpecDto {

    private String cacheName;
    private Duration cacheTtl;
    private Long cacheSize;
}
