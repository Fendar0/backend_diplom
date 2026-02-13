package com.shiptelemetry.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "data_records")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DataRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ship_id", nullable = false)
    private Ship ship;

    @ManyToOne
    @JoinColumn(name = "voyage_id")
    private Voyage voyage;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt = LocalDateTime.now(); // Время сервера

    @Column(name = "data_timestamp")
    private LocalDateTime dataTimestamp; // Время с судна
}
