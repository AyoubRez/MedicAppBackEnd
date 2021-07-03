package com.mdc.medic.dbmigration;

import com.mdc.medic.dbmigration.config.FlywayMigrator;
import com.mdc.medic.dbmigration.config.MigrationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MigrationLauncher {
    @Autowired
    @Qualifier("defaultMigrationProperties")
    public MigrationProperties defaultMigrationProperties;

    @Autowired
    private FlywayMigrator flywayMigrator;

    @PostConstruct
    public void migrate() {
        assert flywayMigrator != null;

        flywayMigrator.migrate(defaultMigrationProperties);
    }
}
