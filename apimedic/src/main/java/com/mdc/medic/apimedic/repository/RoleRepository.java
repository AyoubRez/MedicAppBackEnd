package com.mdc.medic.apimedic.repository;

import com.mdc.medic.apimedic.models.ERole;
import com.mdc.medic.apimedic.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
