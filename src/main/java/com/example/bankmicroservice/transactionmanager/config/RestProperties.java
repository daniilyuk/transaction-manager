package com.example.bankmicroservice.transactionmanager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "rest")
public class RestProperties {
    private List<Endpoint> endpoints;
}
