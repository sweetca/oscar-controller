package com.oscar.controller;

import com.mongodb.MongoClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClientOptions mongoOptions() {
        return MongoClientOptions
                .builder()
                .socketTimeout(120000)
                .socketKeepAlive(true)
                .maxConnectionIdleTime(120000)
                .maxConnectionLifeTime(0)
                .connectionsPerHost(10)
                .minConnectionsPerHost(1)
                .build();
    }
}
