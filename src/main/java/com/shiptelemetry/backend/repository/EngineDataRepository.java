package com.shiptelemetry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shiptelemetry.backend.entity.EngineData;

import java.util.List;

public interface EngineDataRepository extends JpaRepository<EngineData, Long> {
    List<EngineData> findTop100ByShipIdOrderByDataTimestampDesc(Long shipId);
}
