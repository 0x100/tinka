package ru.ilysenko.tinka.config;

import io.swagger.client.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import ru.ilysenko.tinka.interceptor.LoggingRequestInterceptor;

@Configuration
@RequiredArgsConstructor
@ComponentScan("io.swagger.client")
public class ApiClientConfiguration {

    private final ApiClientProperties properties;

    @Bean
    public ApiClient apiClient() {
        ApiClient apiClient = new ApiClient();
        if (Boolean.TRUE.equals(properties.getUseSandbox())) {
            String basePath = apiClient.getBasePath() + "sandbox/";
            apiClient.setBasePath(basePath);
            apiClient.setAccessToken(properties.getSandboxToken());
        } else {
            apiClient.setAccessToken(properties.getExchangeToken());
        }
        return apiClient;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()))
                .interceptors(new LoggingRequestInterceptor())
                .build();
    }
}
