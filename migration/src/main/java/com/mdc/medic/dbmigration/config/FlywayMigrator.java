package com.mdc.medic.dbmigration.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

@Component
public class FlywayMigrator {
    private Logger logger = LogManager.getLogger(getClass());

    public void migrate(MigrationProperties properties) {
        logger.debug("Hello, migration starts with properties enabled '{}'", properties.isEnabled());
        if (!properties.isEnabled()) {
            return;
        }

        Flyway flyway = new Flyway();
        flyway.setDataSource(properties.getUrl(), properties.getUser(), properties.getPassword());
        if (properties.getTable() != null) {
            flyway.setTable(properties.getTable());
        }
        if (properties.getLocations() != null) {
            flyway.setLocations(properties.getLocations().split(","));
        }
        flyway.setValidateOnMigrate(properties.isValidateOnMigrate());
        flyway.setBaselineOnMigrate(properties.isBaselineOnMigrate());
        flyway.migrate();
    }
}
