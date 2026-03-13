package com.shiptelemetry.backend.repository;

import com.shiptelemetry.backend.entity.EngineData;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shiptelemetry.backend.entity.EnvironmentData;

import java.time.LocalDateTime;
import java.util.List;

public interface EnvironmentDataRepository extends JpaRepository<EnvironmentData, Long> {
    List<EnvironmentData> findTop100ByShipIdOrderByDataTimestampDesc(Long shipId);
    List<EnvironmentData> findAllByShipIdAndDataTimestampBetweenOrderByDataTimestampAsc(Long shipId, LocalDateTime start, LocalDateTime end);
}
