package com.shiptelemetry.backend.controller;

import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VoyageDto {
    private String mmsi;
    private String departurePort;
    private String destinationPort;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
}
