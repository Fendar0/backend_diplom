package com.shiptelemetry.backend.controller;

import com.shiptelemetry.backend.common.Status;
import com.shiptelemetry.backend.entity.*;
import com.shiptelemetry.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final ShipRepository shipRepository;
    private final EngineDataRepository engineRepository;
    private final PositionRepository positionRepository;
    private final EnvironmentDataRepository environmentRepository;
    private final VoyageRepository voyageRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    @GetMapping("/")
    public String root() {
        return "redirect:/ships";
    }

    @GetMapping("/ships")
    public String shipsPage(Model model) {
        model.addAttribute("ships", shipRepository.findAll());
        return "ships";
    }

    @GetMapping("/ship/{id}")
    public String shipDetails(@PathVariable Long id,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                              Model model) {

        Ship ship = shipRepository.findById(id).orElse(null);
        if (ship == null) return "redirect:/ships";
        model.addAttribute("ship", ship);

        List<EngineData> engineHistory = new ArrayList<>();
        List<Position> positionHistory = new ArrayList<>();
        List<EnvironmentData> envHistory = new ArrayList<>();

        if (start == null || end == null) {
            // Без фильтра: берем последние 100 записей в desc order
            engineHistory = engineRepository.findTop100ByShipIdOrderByDataTimestampDesc(id);
            positionHistory = positionRepository.findTop100ByShipIdOrderByDataTimestampDesc(id);
            envHistory = environmentRepository.findTop100ByShipIdOrderByDataTimestampDesc(id);
            // Разворачиваем engine для графиков (от старых к новым)
            Collections.reverse(engineHistory);
        } else {
            // С фильтром: берем записи в периоде в asc order
            engineHistory = engineRepository.findAllByShipIdAndDataTimestampBetweenOrderByDataTimestampAsc(id, start, end);
            positionHistory = positionRepository.findAllByShipIdAndDataTimestampBetweenOrderByDataTimestampAsc(id, start, end);
            envHistory = environmentRepository.findAllByShipIdAndDataTimestampBetweenOrderByDataTimestampAsc(id, start, end);
        }

        // Последняя позиция: самый последний в списке (для desc - get(0), для asc - get(size-1))
        Position lastPos = !positionHistory.isEmpty() ?
                (start == null || end == null ? positionHistory.get(0) : positionHistory.get(positionHistory.size() - 1)) : null;

        // Последние данные окружения: самый последний в списке
        EnvironmentData latestEnv = !envHistory.isEmpty() ?
                (start == null || end == null ? envHistory.get(0) : envHistory.get(envHistory.size() - 1)) : null;

        // Информация о рейсе (берем последний активный или просто самый последний, с null-safe)
        Voyage currentVoyage = voyageRepository.findFirstByShipIdAndVoyageStatusNotOrderByDepartureTimeDesc(id, Status.COMPLETED)
                .orElseGet(() -> voyageRepository.findAll().stream()
                        .filter(v -> v.getShip().getId().equals(id))
                        .max(Comparator.comparing(Voyage::getDepartureTime))
                        .orElse(null));

        model.addAttribute("lastPos", lastPos);
        model.addAttribute("latestEnv", latestEnv);
        model.addAttribute("voyage", currentVoyage);
        model.addAttribute("engineData", mapEngineData(engineHistory));
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "dashboard";
    }

    private List<Map<String, Object>> mapEngineData(List<EngineData> data) {
        return data.stream().map(e -> {
            Map<String, Object> m = new HashMap<>();
            m.put("t", e.getDataTimestamp() != null ? e.getDataTimestamp().format(DATE_FORMATTER) : "");
            m.put("rpm", e.getRpm());
            m.put("fRate", e.getFuelRate());
            m.put("fTotal", e.getFuelTotal());
            m.put("fRem", e.getFuelRemaining());
            m.put("pwr", e.getPowerKw());
            m.put("cTemp", e.getCoolantTemp());
            m.put("oil", e.getOilPressure());
            return m;
        }).collect(Collectors.toList());
    }
}