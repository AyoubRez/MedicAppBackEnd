package com.mdc.medic.apimedic.repository;

import com.mdc.medic.apimedic.models.ChangePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChangePasswordRepository extends JpaRepository<ChangePassword,Long> {

    Optional<ChangePassword> findById(Long id);

}
