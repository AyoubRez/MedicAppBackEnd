package com.mdc.medic.apimedic.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"${com.mdc.medic.version.config}", "version.properties"}, ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "com.mdc.medic")
public class VersionConfig {
    @Value("${com.mdc.medic.version.compatible.android}")
    private String compatibleAndroid;

    @Value("${com.mdc.medic.version.compatible.ios}")
    private String compatibleIos;

    public String getCompatibleAndroid() {
        return compatibleAndroid;
    }

    public void setCompatibleAndroid(String compatibleAndroid) {
        this.compatibleAndroid = compatibleAndroid;
    }

    public String getCompatibleIos() {
        return compatibleIos;
    }

    public void setCompatibleIos(String compatibleIos) {
        this.compatibleIos = compatibleIos;
    }
}
