package ru.ilysenko.tinka.config;

import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZonedDateTime;
import ru.ilysenko.tinka.interceptor.LoggingRequestInterceptor;
import ru.ilysenko.tinka.interceptor.RequestEncodingInterceptor;
import ru.tinkoff.invest.client.CustomInstantDeserializer;

@Configuration
public class RestConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()))
                .interceptors(new RequestEncodingInterceptor(), new LoggingRequestInterceptor())
                .defaultMessageConverters()
                .build();
        addDateDeserializers(restTemplate);

        return restTemplate;
    }

    private void addDateDeserializers(RestTemplate restTemplate) {
        restTemplate.getMessageConverters().stream()
                .filter(c -> c instanceof AbstractJackson2HttpMessageConverter)
                .findFirst()
                .map(c -> (AbstractJackson2HttpMessageConverter) c)
                .map(AbstractJackson2HttpMessageConverter::getObjectMapper)
                .ifPresent(m -> {
                    ThreeTenModule module = new ThreeTenModule();
                    module.addDeserializer(Instant.class, CustomInstantDeserializer.INSTANT);
                    module.addDeserializer(OffsetDateTime.class, CustomInstantDeserializer.OFFSET_DATE_TIME);
                    module.addDeserializer(ZonedDateTime.class, CustomInstantDeserializer.ZONED_DATE_TIME);
                    m.registerModule(module);
                });
    }
}
