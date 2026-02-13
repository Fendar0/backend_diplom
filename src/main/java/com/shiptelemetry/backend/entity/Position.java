package com.shiptelemetry.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "positions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "ship")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ship_id", nullable = false)
    @JsonIgnore
    private Ship ship;

    @ManyToOne
    @JoinColumn(name = "voyage_id")
    private Voyage voyage;

    @OneToOne
    @JoinColumn(name = "record_id")
    private DataRecord dataRecord;

    @JsonProperty("lat")
    @Column(name = "latitude")
    private Double latitude;

    @JsonProperty("lon")
    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "course")
    private Double course;

    @Column(name = "heading")
    private Double heading;

    @Column(name = "data_timestamp", nullable = false)
    private LocalDateTime dataTimestamp;
}
