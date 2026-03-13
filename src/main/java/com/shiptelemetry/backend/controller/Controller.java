package com.shiptelemetry.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shiptelemetry.backend.service.TelemetryService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    private final TelemetryService telemetryService;

    @PostMapping("/ship-info")
    public ResponseEntity<?> updateFromCompany(@RequestBody ShipDto dto) {
        telemetryService.updateShip(dto);
        return ResponseEntity.ok("Данные судна обновлены");
    }

    @PostMapping("/voyage")
    public ResponseEntity<?> updateVoyage(@RequestBody VoyageDto dto) {
        telemetryService.createOrUpdateVoyage(dto);
        return ResponseEntity.ok("Данные рейса приняты");
    }

    @PostMapping("/telemetry")
    public ResponseEntity<?> receiveTelemetry(@RequestBody TelemetryRequestDto dto) {
        telemetryService.saveTelemetry(dto);
        return ResponseEntity.ok("Телеметрия принята");
    }
}
