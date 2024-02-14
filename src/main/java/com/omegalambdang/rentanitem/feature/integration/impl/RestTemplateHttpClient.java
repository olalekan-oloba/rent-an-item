package com.omegalambdang.rentanitem.feature.integration.impl;

import com.omegalambdang.rentanitem.feature.integration.HttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateHttpClient implements HttpClient {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;

    public RestTemplateHttpClient(RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
    }

    @Override
    public <T> T get(String url, HttpHeaders headers, Class<T> response) {
        return retryTemplate
                .execute(context -> {
                            System.out.println("called get");
                            return call(url, HttpMethod.GET, headers, null, response);
                        }
                );
    }

    @Override
    public <T, S> T post(String url, HttpHeaders headers, S requestBody, Class<T> response) {
        return retryTemplate
                .execute(context -> {
                            System.out.println("called post");
                            return call(url, HttpMethod.POST, headers, requestBody, response);
                        }
                );
    }

    @Override
    public <T> T delete(String url, HttpHeaders headers, Class<T> response) {
        return retryTemplate
                .execute(context -> {
                            System.out.println("called");
                            return call(url, HttpMethod.DELETE, headers, null, response);
                        }
                );
    }

    @Override
    public <T, S> T put(String url, HttpHeaders headers, S requestBody, Class<T> response) {
        return call(url, HttpMethod.PUT, headers, requestBody, response);
    }

    public <T, S> T call(String url, HttpMethod httpMethod, HttpHeaders headers, @Nullable S requestBody, Class<T> responseType, Object... urlVariables) {

        HttpEntity<S> request = null;

        if (HttpMethod.GET == httpMethod) {
            request = new HttpEntity<>(headers);
        } else {
            request = new HttpEntity<>(requestBody, headers);
        }

        var finalRequest = request;
        ResponseEntity<T> resp =
                restTemplate.exchange(url, httpMethod, finalRequest,
                        responseType, urlVariables);
        return resp.getBody();
    }

}



