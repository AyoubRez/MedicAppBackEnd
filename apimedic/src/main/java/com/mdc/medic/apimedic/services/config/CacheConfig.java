package com.mdc.medic.apimedic.services.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "cache")
@PropertySource(value = {"${com.mdc.medic.cache.config}", "cache.properties"}, ignoreResourceNotFound = true)
public class CacheConfig {
    @Value("${redis.embedded.active}")
    private boolean embeddedRedisActive;

    public boolean isEmbeddedRedisActive() {
        return embeddedRedisActive;
    }

}
