package com.shiptelemetry.backend.controller;


import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ShipDto {
    private String mmsi;

    // Данные судна
    private String name;
    private String imo;
    private String shipType;
    private Double length;
    private Double width;
    private Double draft;
    private String flag;
}
