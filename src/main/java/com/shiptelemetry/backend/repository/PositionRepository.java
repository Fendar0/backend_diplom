package com.shiptelemetry.backend.repository;

import com.shiptelemetry.backend.entity.EnvironmentData;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shiptelemetry.backend.entity.Position;

import java.time.LocalDateTime;
import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findTop100ByShipIdOrderByDataTimestampDesc(Long shipId);
    List<Position> findAllByShipIdAndDataTimestampBetweenOrderByDataTimestampAsc(Long shipId, LocalDateTime start, LocalDateTime end);
}

