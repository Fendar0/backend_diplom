package com.shiptelemetry.backend.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "ships")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 9)
    private String mmsi;

    @Column(unique = true, length = 7)
    private String imo;

    @Column(nullable = false)
    private String name;

    @Column(name = "ship_type")
    private String shipType;

    @Column(name = "length")
    private Double length;

    @Column(name = "width")
    private Double width;

    @Column(name = "draft")
    private Double draft;

    @Column(name = "flag")
    private String flag;

    @OneToMany(mappedBy = "ship")
    private List<Voyage> voyages;
}
