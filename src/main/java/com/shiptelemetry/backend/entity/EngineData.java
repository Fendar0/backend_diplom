package com.shiptelemetry.backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "engine_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EngineData {
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

    @JsonProperty("rpm")
    @Column(name = "engine_rpm")
    private Double rpm;

    @Column(name = "fuel_rate")
    private Double fuelRate;

    @Column(name = "fuel_total")
    private Double fuelTotal;

    @Column(name = "fuel_remaining")
    private Double fuelRemaining;

    @Column(name = "power_kw")
    private Double powerKw;

    @Column(name = "coolant_temp")
    private Double coolantTemp;

    @Column(name = "oil_pressure")
    private Double oilPressure;

    @Column(name = "data_timestamp", nullable = false)
    private LocalDateTime dataTimestamp;
}
