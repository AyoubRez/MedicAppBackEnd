package com.mdc.medic.dbmigration.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAutoConfiguration
@PropertySource(value = {"${com.mdc.medic.application.config}", "application.properties"}, ignoreResourceNotFound = true)
public class MigrationConfiguration {
    private Logger logger = LogManager.getLogger(getClass());

    @Bean(name = "defaultMigrationProperties")
    public MigrationProperties defaultMigrationProperties() {
        logger.debug("creating default migration configuration");
        return new MigrationProperties();
    }

}
