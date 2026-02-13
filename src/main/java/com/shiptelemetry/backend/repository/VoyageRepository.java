package com.shiptelemetry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shiptelemetry.backend.entity.Voyage;
import java.util.Optional;

public interface VoyageRepository extends JpaRepository<Voyage, Long> {
    Optional<Voyage> findFirstByShipIdAndVoyageStatusOrderByActualDepartureDesc(Long shipId, String status);
}
