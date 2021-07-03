package com.mdc.medic.apimedic.repository;

import com.mdc.medic.apimedic.models.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectorRepository extends JpaRepository<Sector,Long> {

    Optional<Sector> findById(Long id);

    Optional<Sector> findBySectorCode(String sectorCode);

    List<Sector> findByIsDeleted(Boolean isDeleted);

    Boolean existsBySectorCode(String sectorCode);
}
