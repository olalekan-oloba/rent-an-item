package com.omegalambdang.rentanitem.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "retry-template-policy")
@Getter
@Setter
public class RetryPropertiesConfig {
    private final Simple simple = new Simple();
    @Getter
    @Setter
    public static class Simple {
      private int maxRetryAttempts=4;
    }

    private final Fixed fixed = new Fixed();
    @Data
    public static class Fixed {
        private int fixedBackoffDuration=1000;//millsec

    }
}
