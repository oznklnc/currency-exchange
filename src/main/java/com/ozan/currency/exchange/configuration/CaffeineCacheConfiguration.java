package com.ozan.currency.exchange.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.ozan.currency.exchange.configuration.properties.CacheSpecProperties;
import com.ozan.currency.exchange.model.dto.CacheSpecDto;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CaffeineCacheConfiguration {

    private final CacheSpecProperties cacheSpecProperties;

    @Bean
    public CacheManager cacheManager() {
        List<CaffeineCache> caffeineCacheList = cacheSpecProperties.getSpecs()
                .values()
                .stream()
                .map(this::buildCache)
                .collect(Collectors.toList());

        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(caffeineCacheList);
        return simpleCacheManager;
    }

    private CaffeineCache buildCache(CacheSpecDto cacheSpecDto) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .expireAfterWrite(cacheSpecDto.getCacheTtl())
                .maximumSize(cacheSpecDto.getCacheSize());
        return new CaffeineCache(cacheSpecDto.getCacheName(), caffeine.build());
    }
}
