package com.jbuncle.mysqlsynchroniser;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigProperties {

    @Bean
    @ConfigurationProperties(prefix = "db")
    public DBProperties dbProperties() {
        return new DBProperties();
    }
}
