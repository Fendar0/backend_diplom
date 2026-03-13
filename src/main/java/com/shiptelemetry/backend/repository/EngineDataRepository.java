package com.shiptelemetry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shiptelemetry.backend.entity.EngineData;

import java.time.LocalDateTime;
import java.util.List;

public interface EngineDataRepository extends JpaRepository<EngineData, Long> {
    List<EngineData> findTop100ByShipIdOrderByDataTimestampDesc(Long shipId);
    List<EngineData> findAllByShipIdAndDataTimestampBetweenOrderByDataTimestampAsc(Long shipId, LocalDateTime start, LocalDateTime end);
}
