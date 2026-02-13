package com.shiptelemetry.backend.controller;

import com.shiptelemetry.backend.entity.*;
import com.shiptelemetry.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final ShipRepository shipRepository;
    private final EngineDataRepository engineRepository;
    private final PositionRepository positionRepository;
    private final EnvironmentDataRepository environmentRepository;

    // 1. Главная страница -> Редирект на список
    @GetMapping("/")
    public String root() {
        return "redirect:/ships";
    }

    // 2. Список судов
    @GetMapping("/ships")
    public String shipsPage(Model model) {
        model.addAttribute("ships", shipRepository.findAll());
        return "ships"; // Ищет ships.html
    }

    // 3. Дашборд с графиками
    @GetMapping("/ship/{id}")
    public String shipDetails(@PathVariable Long id, Model model) {
        Ship ship = shipRepository.findById(id).orElseThrow();

        // Загружаем по 100 последних записей
        var engineHistory = engineRepository.findTop100ByShipIdOrderByDataTimestampDesc(id);
        var posHistory = positionRepository.findTop100ByShipIdOrderByDataTimestampDesc(id);
        var envHistory = environmentRepository.findTop100ByShipIdOrderByDataTimestampDesc(id);

        // Разворачиваем списки, чтобы на графике время шло слева направо
        Collections.reverse(engineHistory);
        Collections.reverse(posHistory);
        Collections.reverse(envHistory);

        // Мапим параметры строго по твоей схеме БД
        model.addAttribute("engineData", engineHistory.stream().map(e -> {
            Map<String, Object> m = new HashMap<>();
            m.put("t", e.getDataTimestamp());
            m.put("rpm", e.getRpm());
            m.put("fRate", e.getFuelRate());
            m.put("fTotal", e.getFuelTotal());
            m.put("fRem", e.getFuelRemaining());
            m.put("pwr", e.getPowerKw());
            m.put("cTemp", e.getCoolantTemp());
            m.put("oil", e.getOilPressure());
            return m;
        }).toList());

        model.addAttribute("posData", posHistory.stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("t", p.getDataTimestamp());
            m.put("spd", p.getSpeed());
            m.put("crs", p.getCourse());
            m.put("hdg", p.getHeading());
            return m;
        }).toList());

        model.addAttribute("envData", envHistory.stream().map(env -> {
            Map<String, Object> m = new HashMap<>();
            m.put("t", env.getDataTimestamp());
            m.put("wSpd", env.getWindSpeed());
            m.put("wDir", env.getWindDirection());
            m.put("aTemp", env.getAirTemp());
            m.put("wTemp", env.getWaterTemp());
            m.put("pres", env.getAirPressure());
            m.put("dpth", env.getWaterDepth());
            m.put("wave", env.getWaveHeight());
            return m;
        }).toList());

        model.addAttribute("ship", ship);
        return "dashboard";
    }
}
