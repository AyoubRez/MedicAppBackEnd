package com.mdc.medic.apimedic.security.adminAccount;

import com.mdc.medic.apimedic.exceptions.FunctionalException;
import com.mdc.medic.apimedic.exceptions.MedicFrontExceptionCode;
import com.mdc.medic.apimedic.models.ERole;
import com.mdc.medic.apimedic.models.Role;
import com.mdc.medic.apimedic.models.Sector;
import com.mdc.medic.apimedic.models.User;
import com.mdc.medic.apimedic.repository.RoleRepository;
import com.mdc.medic.apimedic.repository.SectorRepository;
import com.mdc.medic.apimedic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@PropertySource(value = {"${com.mdc.medic.admin.config}", "admin.properties"}, ignoreResourceNotFound = true)
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Value("${mdc.app.adminMail}")
    private String adminMail;

    @Value("${mdc.app.adminPassword}")
    private String adminPassword;

    @Value("${mdc.app.adminName}")
    private String adminName;

    @Value("${mdc.app.adminPhone}")
    private String adminPhone;

    @Value("${mdc.app.adminSectorCode}")
    private String adminSectorCode;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    SectorRepository sectorRepository;

    @Override
    public void run(String...args) throws Exception {

        Sector adminSector=sectorRepository.findBySectorCode(adminSectorCode)
                .orElseThrow(() -> new FunctionalException(MedicFrontExceptionCode.BRANCH_CODE_NOT_FOUND,"BRANCH_CODE_NOT_FOUND"));
        if(!userRepository.existsByUsername("admin")&&!userRepository.existsByEmail(adminMail)){
            User admin = new User(
                    "admin",
                    adminMail,
                    encoder.encode(adminPassword),
                    adminName,
                    adminPhone,
                    adminSector,
                    new Date(),
                    new Date(),
                    true,
                    false,
                    false
            );
            Set<Role> roles = new HashSet<>();
            Role role = roleRepository.findByName(ERole.ROLE_ADMIN).get();
            roles.add(role);
            admin.setRoles(roles);
            userRepository.save(admin);
        }

    }
}