package com.shiptelemetry.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shiptelemetry.backend.service.TelemetryService;
import com.shiptelemetry.backend.controller.TelemetryRequestDto;

@RestController
@RequestMapping("/api/telemetry")
@RequiredArgsConstructor
public class controller {
    private final TelemetryService telemetryService;

    @PostMapping
    public ResponseEntity<String> receiveData(@RequestBody TelemetryRequestDto dto) {
        try {
            telemetryService.saveTelemetry(dto);
            return ResponseEntity.ok("Data saved successfully");
        } catch (Exception e) {
            e.printStackTrace(); // Для отладки в консоль
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
