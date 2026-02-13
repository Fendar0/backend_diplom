package com.shiptelemetry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shiptelemetry.backend.entity.EnvironmentData;

public interface EnvironmentDataRepository extends JpaRepository<EnvironmentData, Long> {
}
