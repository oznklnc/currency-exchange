package com.ozan.currency.exchange.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@Data
@ConfigurationProperties(prefix = "spring.datasource")
@EntityScan("com.ozan.currency.exchange")
@EnableJpaRepositories("com.ozan.currency.exchange")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class DatabaseConfiguration {

    private String url;
    private String driverClassName;
    private String username;
    private String password;

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }

    private HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setPoolName("my db pool");
        config.setMaximumPoolSize(10);
        return config;
    }
}
