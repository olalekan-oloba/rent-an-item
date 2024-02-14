package com.omegalambdang.rentanitem.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@Getter
@Setter
public class PropertiesConfig {
    private final Jwt jwt = new Jwt();
    @Getter
    @Setter
    public static class Jwt {
        private final Authorities authorities = new Authorities();
        private final Token token = new Token();
        @Getter
        @Setter
        public static class Authorities {
            private String key;
        }
        @Getter
        @Setter
        public static class Token {
            private String secretKey;
            private long expireLength;
        }

    }

    private final Api api = new Api();
    @Data
    public static class Api {
        private String urlDomain;
        private String moduleName;
        private String basePathApi;
        private String basePathAdmin;
        private String basePathPrivate;
        private double version;
    }
}
