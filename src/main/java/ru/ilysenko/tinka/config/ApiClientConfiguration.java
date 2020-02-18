package ru.ilysenko.tinka.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.invest.client.ApiClient;

@Configuration
@ComponentScan("ru.tinkoff.invest")
public class ApiClientConfiguration {

    public ApiClientConfiguration(ApiClient apiClient, ApiClientProperties properties) {
        if (Boolean.TRUE.equals(properties.getUseSandbox())) {
            String basePath = apiClient.getBasePath() + "sandbox/";
            apiClient.setBasePath(basePath);
            apiClient.setAccessToken(properties.getSandboxToken());
        } else {
            apiClient.setAccessToken(properties.getExchangeToken());
        }
    }
}
