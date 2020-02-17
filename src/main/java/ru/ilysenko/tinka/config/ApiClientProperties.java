package ru.ilysenko.tinka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("client")
public class ApiClientProperties {
    private Boolean useSandbox;
    private String sandboxToken;
    private String exchangeToken;

}
