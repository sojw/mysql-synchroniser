package com.jbuncle.mysqlsynchroniser;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "db")
public class DBProperties {
    private DataSoruce source;
    private DataSoruce target;

    @Getter
    @Setter
    public static class DataSoruce {
        private String host;
        private String database;
        private String user;
        private String password;
        private String port;
    }
}
