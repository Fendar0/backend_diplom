package com.shiptelemetry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shiptelemetry.backend.entity.EngineData;

public interface EngineDataRepository extends JpaRepository<EngineData, Long> {
}
