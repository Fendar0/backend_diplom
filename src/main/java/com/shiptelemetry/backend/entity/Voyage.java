package com.shiptelemetry.backend.entity;

import com.shiptelemetry.backend.common.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voyages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Voyage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ship_id", nullable = false)
    private Ship ship;

    @Column(name = "destination_port")
    private String destinationPort;

    @Column(name = "departure_port")
    private String departurePort;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    //@Future
    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "voyage_status")
    private Status voyageStatus; // Можно заменить на Enum
}
