package com.shiptelemetry.backend.entity;

import jakarta.persistence.*;
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

    @Column(name = "eta")
    private LocalDateTime eta;

    @Column(name = "departure_port")
    private String departurePort;

    @Column(name = "actual_departure")
    private LocalDateTime actualDeparture;

    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    @Column(name = "voyage_status")
    private String voyageStatus; // Можно заменить на Enum
}
