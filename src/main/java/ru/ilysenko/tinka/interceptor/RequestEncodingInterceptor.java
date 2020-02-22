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

public class RequestEncodingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    @SneakyThrows
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
        HttpRequest encodedRequest = encode(request);
        return execution.execute(encodedRequest, body);
    }

    private HttpRequest encode(HttpRequest request) {
        return new HttpRequestWrapper(request) {
                private URI uri;

                @Override
                public URI getURI() {
                    if (uri == null) {
                        uri = encodeUri(request.getURI()); // Caching the request uri for a recall of the getURI method from another interceptors
                    }
                    return uri;
                }
            };
    }

    private URI encodeUri(URI uri) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(uri);
        for (NameValuePair param : URLEncodedUtils.parse(uri, StandardCharsets.UTF_8)) {
            uriComponentsBuilder.replaceQueryParam(param.getName(), URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8).replace("+", "%2B"));
        }
        return uriComponentsBuilder.build(true).toUri();
    }
}
