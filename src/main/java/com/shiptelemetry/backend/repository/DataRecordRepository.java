package com.shiptelemetry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shiptelemetry.backend.entity.DataRecord;

public interface DataRecordRepository extends JpaRepository<DataRecord, Long> {
}