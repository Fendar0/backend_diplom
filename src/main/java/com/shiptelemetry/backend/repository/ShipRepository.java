package com.shiptelemetry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shiptelemetry.backend.entity.Ship;
import java.util.Optional;

public interface ShipRepository extends JpaRepository<Ship, Long> {

    Optional<Ship> findByMmsi(String mmsi);
}
