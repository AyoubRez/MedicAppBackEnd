package com.mdc.medic.apimedic.repository;

import com.mdc.medic.apimedic.models.ChangePasswordOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChangePasswordOtpRepository extends JpaRepository<ChangePasswordOtp,Long> {

    Optional<ChangePasswordOtp> findByOtpId(Long otpId);



}
