package com.mdc.medic.apimedic.repository;

import com.mdc.medic.apimedic.models.RefreshToken;
import com.mdc.medic.apimedic.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);


    @Transactional
    @Modifying
    @Query("update REFRESH_TOKEN u set u.isUsed = ?1 where u.user.id = ?2")
    void setIsUsedByUserId(Boolean isUsed, Long id);

    @Transactional
    Optional<RefreshToken> deleteByUserId(Long id);

    int deleteByUser(User user);
}
