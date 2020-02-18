/*
 * The GNU Affero General Public License v3.0 (AGPL-3.0)
 *
 * Copyright (c) 2020 Ilya Lysenko
 *
 * Permissions of this strongest copyleft license are conditioned on making available complete source code of licensed
 * works and modifications, which include larger works using a licensed work, under the same license.
 * Copyright and license notices must be preserved. Contributors provide an express grant of patent rights.
 * When a modified version is used to provide a service over a network,  the complete source code of the modified
 * version must be made available.
 */
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
