package com.shiptelemetry.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Engine;
import org.springframework.stereotype.Service;
import com.shiptelemetry.backend.controller.TelemetryRequestDto;
import com.shiptelemetry.backend.entity.*;
import com.shiptelemetry.backend.repository.*;
import java.time.LocalDateTime;
import com.shiptelemetry.backend.entity.*;

@Service
@RequiredArgsConstructor
public class TelemetryService {
    private final ShipRepository shipRepository;
    private final VoyageRepository voyageRepository;
    private final DataRecordRepository dataRecordRepository;
    private final PositionRepository positionRepository;
    private final EngineDataRepository engineRepository;
    private final EnvironmentDataRepository environmentDataRepository;

    @Transactional // Если где-то ошибка, откатываем всё назад
    public void saveTelemetry(TelemetryRequestDto dto) {
        System.out.println("DEBUG: Принятый DTO -> " + dto.toString());
        if (dto.getDataTimestamp() == null) {
            throw new RuntimeException("Timestamp is missing in the request!");
        }
        // 1. Находим судно или кидаем ошибку
        Ship ship = shipRepository.findByMmsi(dto.getMmsi())
                .orElseThrow(() -> new RuntimeException("Судно с MMSI " + dto.getMmsi() + " не найдено"));

        // 2. Находим текущий активный рейс (упрощенно)
        Voyage currentVoyage = voyageRepository
                .findFirstByShipIdAndVoyageStatusOrderByActualDepartureDesc(ship.getId(), "IN_PROGRESS")
                .orElse(null); // Рейса может и не быть (стоянка)

        // 3. Создаем главную запись (Header)
        DataRecord record = new DataRecord();
        record.setShip(ship);
        record.setVoyage(currentVoyage);
        record.setReceivedAt(LocalDateTime.now());
        record.setDataTimestamp(dto.getDataTimestamp());
        dataRecordRepository.save(record);

        // 4. Сохраняем позицию, если она пришла
        if (dto.getPosition() != null) {
            Position pos = new Position();
            pos.setShip(ship);       // Дублирование данных согласно вашей схеме БД
            pos.setVoyage(currentVoyage);
            pos.setDataRecord(record);
            pos.setDataTimestamp(dto.getDataTimestamp());

            pos.setLatitude(dto.getPosition().latitude);
            pos.setLongitude(dto.getPosition().longitude);
            pos.setSpeed(dto.getPosition().speed);
            pos.setCourse(dto.getPosition().course);
            pos.setHeading(dto.getPosition().heading);

            positionRepository.save(pos);
        }

        if (dto.getEngine() != null) {
            EngineData eng = new EngineData();
            eng.setShip(ship);
            eng.setVoyage(currentVoyage);
            eng.setDataRecord(record);
            eng.setDataTimestamp(dto.getDataTimestamp());

            eng.setRpm(dto.getEngine().rpm);
            eng.setFuelRate(dto.getEngine().fuelRate);
            eng.setFuelTotal(dto.getEngine().fuelTotal);
            eng.setFuelRemaining(dto.getEngine().fuelRemaining);
            eng.setPowerKw(dto.getEngine().powerKw);
            eng.setCoolantTemp(dto.getEngine().coolantTemp);
            eng.setOilPressure(dto.getEngine().oilPressure);
            eng.setDataTimestamp(dto.getDataTimestamp());

            engineRepository.save(eng);
        }

        if (dto.getEnvironment() != null) {
            EnvironmentData env = new EnvironmentData();
            env.setShip(ship);
            env.setVoyage(currentVoyage);
            env.setDataRecord(record);
            env.setDataTimestamp(dto.dataTimestamp);

            env.setWindSpeed(dto.getEnvironment().windSpeed);
            env.setWindDirection(dto.getEnvironment().windDirection);
            env.setAirTemp(dto.getEnvironment().airTemp);
            env.setWaterTemp(dto.getEnvironment().waterTemp);
            env.setAirPressure(dto.getEnvironment().airPressure);
            env.setWaterDepth(dto.getEnvironment().waterDepth);
            env.setWaveHeight(dto.getEnvironment().waveHeight);

            environmentDataRepository.save(env);
        }
    }
}
