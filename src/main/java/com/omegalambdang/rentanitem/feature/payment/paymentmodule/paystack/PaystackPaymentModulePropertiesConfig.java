package com.omegalambdang.rentanitem.feature.payment.paymentmodule.paystack;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "payment-module.paystack")
@Getter
@Setter
public class PaystackPaymentModulePropertiesConfig {
    private String secretKey;
    private String baseUrl;
}
