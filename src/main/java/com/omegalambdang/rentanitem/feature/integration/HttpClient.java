package com.omegalambdang.rentanitem.feature.integration;

import org.springframework.http.HttpHeaders;

public interface HttpClient {
     <T> T get(String url, HttpHeaders header, Class<T> response);

    <T,S> T post(String url, HttpHeaders buildHeaders, S body, Class<T> stringClass);

    <T> T delete(String url, HttpHeaders buildHeaders, Class<T> stringClass);
    <T,S> T put(String url, HttpHeaders buildHeaders, S body, Class<T> stringClass);
}
