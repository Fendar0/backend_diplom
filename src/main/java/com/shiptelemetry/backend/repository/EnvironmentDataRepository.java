package com.shiptelemetry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shiptelemetry.backend.entity.EnvironmentData;

import java.util.List;

public interface EnvironmentDataRepository extends JpaRepository<EnvironmentData, Long> {
    List<EnvironmentData> findTop100ByShipIdOrderByDataTimestampDesc(Long shipId);
}
