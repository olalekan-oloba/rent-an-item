package com.omegalambdang.rentanitem.config;

import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.web.client.HttpStatusCodeException;

@Configuration
public class RetryConfig {

    private final SimpleRetryPolicy simpleRetryPolicy;
    private final NeverRetryPolicy neverRetryPolicy = new NeverRetryPolicy();
    private final FixedBackOffPolicy fixedBackOffPolicy;
    private final ExponentialBackOff exponentialBackOff;

    public RetryConfig( RetryPropertiesConfig retryPropertiesConfig) {

        simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(retryPropertiesConfig.getSimple().getMaxRetryAttempts());

        fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(retryPropertiesConfig.getFixed().getFixedBackoffDuration());

        exponentialBackOff= new ExponentialBackOff();
    }

    @Bean
    public RetryTemplate retryTemplate() {

        RetryTemplate retryTemplate = new RetryTemplate();
        ExceptionClassifierRetryPolicy policy = new ExceptionClassifierRetryPolicy();
        policy.setExceptionClassifier(configureStatusCodeBasedRetryPolicy());
        retryTemplate.setRetryPolicy(policy);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        return retryTemplate;
    }


    private Classifier<Throwable, RetryPolicy> configureStatusCodeBasedRetryPolicy() {
        return throwable -> {
            if (throwable instanceof HttpStatusCodeException exception) {
                return getRetryPolicyForStatus((HttpStatus) exception.getStatusCode());
            }
            return simpleRetryPolicy;
        };
    }

    private RetryPolicy getRetryPolicyForStatus(HttpStatus httpStatus) {
        return switch (httpStatus) {
            case BAD_GATEWAY, SERVICE_UNAVAILABLE, INTERNAL_SERVER_ERROR, GATEWAY_TIMEOUT ->
                    simpleRetryPolicy;
            default -> neverRetryPolicy;
        };
    }

}