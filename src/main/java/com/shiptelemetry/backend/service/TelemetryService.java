package com.shiptelemetry.backend.service;

import com.shiptelemetry.backend.common.Status;
import com.shiptelemetry.backend.controller.ShipDto;
import com.shiptelemetry.backend.controller.VoyageDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.shiptelemetry.backend.controller.TelemetryRequestDto;
import com.shiptelemetry.backend.entity.*;
import com.shiptelemetry.backend.repository.*;
import java.time.LocalDateTime;
import com.shiptelemetry.backend.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelemetryService {
    private final ShipRepository shipRepository;
    private final VoyageRepository voyageRepository;
    private final DataRecordRepository dataRecordRepository;
    private final PositionRepository positionRepository;
    private final EngineDataRepository engineRepository;
    private final EnvironmentDataRepository environmentDataRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateVoyageStatuses() {
        LocalDateTime now = LocalDateTime.now();

        List<Voyage> startingVoyages = voyageRepository.findAllByVoyageStatusInAndDepartureTimeBefore(
                List.of(Status.PLANNED, Status.DELAY), now);

        startingVoyages.forEach(v -> {
            v.setVoyageStatus(Status.IN_PROGRESS);
            log.info("Рейс {} судна {} начался (после планирования или задержки).", v.getId(), v.getShip().getName());
        });

        List<Voyage> endingVoyages = voyageRepository.findAllByVoyageStatusAndArrivalTimeBefore(Status.IN_PROGRESS, now);
        endingVoyages.forEach(v -> {
            v.setVoyageStatus(Status.COMPLETED);
            log.info("Рейс {} судна {} завершен.", v.getId(), v.getShip().getName());
        });

        voyageRepository.saveAll(startingVoyages);
        voyageRepository.saveAll(endingVoyages);
    }

    @Transactional
    public void createOrUpdateVoyage(VoyageDto dto) {
        Ship ship = shipRepository.findByMmsi(dto.getMmsi())
                .orElseThrow(() -> new RuntimeException("Судно не найдено"));

        Voyage voyage = voyageRepository
                .findFirstByShipIdAndVoyageStatusNotOrderByDepartureTimeDesc(ship.getId(), Status.COMPLETED)
                .orElse(new Voyage());

        LocalDateTime now = LocalDateTime.now();

        if (voyage.getId() != null && voyage.getVoyageStatus() == Status.IN_PROGRESS) {
            if (dto.getDepartureTime().isAfter(now)) {
                voyage.setVoyageStatus(Status.DELAY);
                log.info("Рейс {} судна {} отложен. Статус изменен на DELAY", voyage.getId(), ship.getName());
            }
        }

        voyage.setShip(ship);
        voyage.setDeparturePort(dto.getDeparturePort());
        voyage.setDestinationPort(dto.getDestinationPort());
        voyage.setDepartureTime(dto.getDepartureTime());
        voyage.setArrivalTime(dto.getArrivalTime());

        if (voyage.getId() == null) {
            voyage.setVoyageStatus(Status.PLANNED);
        }

        voyageRepository.save(voyage);
    }

    @Transactional
    public void saveTelemetry(TelemetryRequestDto dto) {
        if (dto.getDataTimestamp() == null) {
            throw new RuntimeException("Timestamp is missing in the request!");
        }

        Ship ship = shipRepository.findByMmsi(dto.getMmsi())
                .orElseThrow(() -> new RuntimeException("Судно с MMSI " + dto.getMmsi() + " не найдено"));

        // Находим текущий рейс (любой статус, кроме COMPLETED)
        Voyage currentVoyage = voyageRepository
                .findFirstByShipIdAndVoyageStatusNotOrderByDepartureTimeDesc(ship.getId(), Status.COMPLETED)
                .orElse(null);

        // 1. Создаем DataRecord (Header)
        DataRecord record = new DataRecord();
        record.setShip(ship);
        record.setVoyage(currentVoyage);
        record.setReceivedAt(LocalDateTime.now());
        record.setDataTimestamp(dto.getDataTimestamp());
        dataRecordRepository.save(record);

        // 2. Сохраняем позицию
        if (dto.getPosition() != null) {
            Position pos = new Position();
            pos.setShip(ship);
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

        // 3. Сохраняем данные двигателя
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
            engineRepository.save(eng);
        }

        // 4. Сохраняем данные окружающей среды
        if (dto.getEnvironment() != null) {
            EnvironmentData env = new EnvironmentData();
            env.setShip(ship);
            env.setVoyage(currentVoyage);
            env.setDataRecord(record);
            env.setDataTimestamp(dto.getDataTimestamp());
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

    @Transactional
    public void updateShip(ShipDto dto) {
        Ship ship = shipRepository.findByMmsi(dto.getMmsi())
                .orElseGet(() -> {
                    Ship s = new Ship();
                    s.setMmsi(dto.getMmsi());
                    return s;
                });

        if (dto.getName() != null) ship.setName(dto.getName());
        if (dto.getImo() != null) ship.setImo(dto.getImo());
        if (dto.getShipType() != null) ship.setShipType(dto.getShipType());
        if (dto.getLength() != null) ship.setLength(dto.getLength());
        if (dto.getWidth() != null) ship.setWidth(dto.getWidth());
        if (dto.getDraft() != null) ship.setDraft(dto.getDraft());
        if (dto.getFlag() != null) ship.setFlag(dto.getFlag());
        shipRepository.save(ship);
    }
}
