package com.mdc.medic.apimedic.repository;


import com.mdc.medic.apimedic.models.TokenBlackList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListTokenRepository extends CrudRepository<TokenBlackList, Long> {
    @Override
    Optional<TokenBlackList> findById(Long id);

    Optional<TokenBlackList> findByToken(String token);

}
