package ru.ilysenko.tinka.interceptor;

import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestEncodingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    @SneakyThrows
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
        HttpRequest encodedRequest = new HttpRequestWrapper(request) {
            private URI uri;

            @Override
            public URI getURI() {
                if (uri == null) {
                    uri = encodeUri(request.getURI());
                }
                return uri;
            }
        };
        return execution.execute(encodedRequest, body);
    }

    @SneakyThrows
    private URI encodeUri(URI uri) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(uri);
        List<NameValuePair> parameters = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);
        for (NameValuePair param : parameters) {
            uriComponentsBuilder.replaceQueryParam(param.getName(), URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8).replace("+", "%2B"));
        }
        return uriComponentsBuilder.build(true).toUri();
    }
}
