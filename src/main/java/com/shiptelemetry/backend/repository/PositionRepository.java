package com.shiptelemetry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shiptelemetry.backend.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {
}

