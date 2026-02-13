package com.shiptelemetry.backend.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TelemetryRequestDto {
    public String mmsi;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("timestamp")
    public LocalDateTime dataTimestamp;

    public PositionDto position;
    public EngineDto engine;
    public EnvironmentDto environment;

    public static class PositionDto {
        public Double latitude;
        public Double longitude;
        public Double speed;
        public Double course;
        public Double heading;
    }

    public static class EngineDto {
        public Double rpm;
        public Double fuelRate;
        public Double fuelTotal;
        public Double fuelRemaining;
        public Double powerKw;
        public Double coolantTemp;
        public Double oilPressure;
    }

    public static class EnvironmentDto {
        public Double windSpeed;
        public Double windDirection;
        public Double airTemp;
        public Double waterTemp;
        public Double airPressure;
        public Double waterDepth;
        public Double waveHeight;
    }
}
