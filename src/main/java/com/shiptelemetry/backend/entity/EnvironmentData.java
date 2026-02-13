package com.shiptelemetry.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "environment_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EnvironmentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ship_id", nullable = false)
    private Ship ship;

    @ManyToOne
    @JoinColumn(name = "voyage_id")
    private Voyage voyage;

    @OneToOne
    @JoinColumn(name = "record_id")
    private DataRecord dataRecord;

    @Column(name = "wind_speed")
    private Double windSpeed;

    @Column(name = "wind_direction")
    private Double windDirection;

    @Column(name = "air_temp")
    private Double airTemp;

    @Column(name = "water_temp")
    private Double waterTemp;

    @Column(name = "air_pressure")
    private Double airPressure;

    @Column(name = "water_depth")
    private Double waterDepth;

    @Column(name = "wave_height")
    private Double waveHeight;

    @Column(name = "data_timestamp", nullable = false)
    private LocalDateTime dataTimestamp;
}
