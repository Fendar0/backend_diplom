package com.shiptelemetry.backend.repository;

import com.shiptelemetry.backend.common.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shiptelemetry.backend.entity.Voyage;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

public interface VoyageRepository extends JpaRepository<Voyage, Long> {
    // Для нахождения рейсов в нескольких статусах (PLANNED, DELAY)
    List<Voyage> findAllByVoyageStatusInAndDepartureTimeBefore(List<Status> statuses, LocalDateTime now);

    List<Voyage> findAllByVoyageStatusAndArrivalTimeBefore(Status status, LocalDateTime now);

    Optional<Voyage> findFirstByShipIdAndVoyageStatusNotOrderByDepartureTimeDesc(Long shipId, Status status);
}
