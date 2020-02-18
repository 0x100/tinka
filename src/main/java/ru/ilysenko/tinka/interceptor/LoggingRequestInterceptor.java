package ru.ilysenko.tinka.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {
    private ObjectMapper objectMapper;

    public LoggingRequestInterceptor() {
        objectMapper = new ObjectMapper();
    }

    @Override
    @SneakyThrows
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
        traceRequest(request, body);

        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);

        return response;
    }

    @SneakyThrows
    private void traceRequest(HttpRequest request, byte[] body) {
        String content = new String(body, StandardCharsets.UTF_8);

        log.debug("===========================request begin================================================");
        log.debug("URI         : {}", request.getURI());
        log.debug("Method      : {}", request.getMethod());
        log.debug("Headers     : {}", request.getHeaders());
        log.debug("Request body: {}", beautifyBody(content));
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        String content = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8.name());

        log.debug("============================response begin==========================================");
        log.debug("Status code  : {}", response.getStatusCode());
        log.debug("Status text  : {}", response.getStatusText());
        log.debug("Headers      : {}", response.getHeaders());
        log.debug("Response body: {}", beautifyBody(content));
    }

    private String beautifyBody(String content) throws JsonProcessingException {
        if (StringUtils.hasText(content) && isValidJson(content)) {
            Object json = objectMapper.readValue(content, Object.class);
            return String.format("\n%s", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
        } else {
            return content;
        }
    }

    private boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            return false;
        }
        return true;
    }
}
