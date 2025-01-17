package com.don.bank.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@TestConfiguration
public class FlywayConfig {

    @Bean
    @Primary
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure().dataSource(dataSource).schemas("app_dbi").cleanDisabled(false).load();
    }
}
