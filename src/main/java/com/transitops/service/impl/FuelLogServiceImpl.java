package com.transitops.service.impl;

import com.transitops.dto.FuelLogDTO;
import com.transitops.entity.FuelLog;
import com.transitops.entity.Vehicle;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.FuelLogRepository;
import com.transitops.repository.VehicleRepository;
import com.transitops.service.FuelLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FuelLogServiceImpl implements FuelLogService {

    private final FuelLogRepository fuelLogRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public FuelLogDTO createFuelLog(FuelLogDTO dto) {

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        FuelLog fuelLog = new FuelLog();

        fuelLog.setLiters(dto.getLiters());
        fuelLog.setCost(dto.getCost());

        if (dto.getFuelDate() == null) {
            fuelLog.setFuelDate(LocalDate.now());
        } else {
            fuelLog.setFuelDate(dto.getFuelDate());
        }

        fuelLog.setVehicle(vehicle);

        FuelLog saved = fuelLogRepository.save(fuelLog);
        log.info("Created fuel log id {}", saved.getId());
        return convert(saved);
    }

    @Override
    public FuelLogDTO getFuelLogById(Long id) {

        return convert(
                fuelLogRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Fuel Log not found"))
        );
    }

    @Override
    public List<FuelLogDTO> getAllFuelLogs() {

        return fuelLogRepository.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFuelLog(Long id) {

        if (!fuelLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fuel Log not found");
        }
        fuelLogRepository.deleteById(id);
    }

    private FuelLogDTO convert(FuelLog fuelLog) {

        FuelLogDTO dto = new FuelLogDTO();

        dto.setId(fuelLog.getId());
        dto.setLiters(fuelLog.getLiters());
        dto.setCost(fuelLog.getCost());
        dto.setFuelDate(fuelLog.getFuelDate());
        dto.setVehicleId(fuelLog.getVehicle().getId());

        return dto;
    }
}
