package com.mdc.medic.dbmigration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.mdc.medic","com.mdc"})
@ComponentScan(basePackages = {"com.mdc.medic", "com.mdc"})
public class MdcDBMigrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(MdcDBMigrationApplication.class, args);
    }

}
