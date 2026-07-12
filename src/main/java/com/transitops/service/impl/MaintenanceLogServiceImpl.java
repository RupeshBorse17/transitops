package com.transitops.service.impl;

import com.transitops.dto.MaintenanceLogDTO;
import com.transitops.entity.MaintenanceLog;
import com.transitops.entity.Vehicle;
import com.transitops.enums.MaintenanceStatus;
import com.transitops.enums.VehicleStatus;
import com.transitops.exception.BusinessException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.MaintenanceLogRepository;
import com.transitops.repository.VehicleRepository;
import com.transitops.service.MaintenanceLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaintenanceLogServiceImpl implements MaintenanceLogService {

    private final MaintenanceLogRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public MaintenanceLogDTO createMaintenance(MaintenanceLogDTO dto) {

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        MaintenanceLog maintenanceLog = new MaintenanceLog();

        maintenanceLog.setIssueDescription(dto.getIssueDescription());
        maintenanceLog.setPriority(dto.getPriority());
        maintenanceLog.setTechnicianName(dto.getTechnicianName());
        maintenanceLog.setCost(dto.getCost() == null ? 0 : dto.getCost());
        maintenanceLog.setStatus(MaintenanceStatus.PENDING);
        maintenanceLog.setVehicle(vehicle);

        MaintenanceLog saved = maintenanceRepository.save(maintenanceLog);
        log.info("Created maintenance log id {}", saved.getId());
        return convert(saved);
    }

    @Override
    public MaintenanceLogDTO approveMaintenance(Long id) {

        MaintenanceLog log = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));

        if (log.getVehicle().getStatus() == VehicleStatus.ON_TRIP) {
            throw new BusinessException("Vehicle on trip cannot be moved to maintenance");
        }

        log.setStatus(MaintenanceStatus.APPROVED);

        Vehicle vehicle = log.getVehicle();
        vehicle.setStatus(VehicleStatus.IN_SHOP);

        vehicleRepository.save(vehicle);

        return convert(maintenanceRepository.save(log));
    }

    @Override
    public MaintenanceLogDTO rejectMaintenance(Long id) {

        MaintenanceLog log = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));

        log.setStatus(MaintenanceStatus.REJECTED);

        return convert(maintenanceRepository.save(log));
    }

    @Override
    public MaintenanceLogDTO resolveMaintenance(Long id) {

        MaintenanceLog log = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));

        log.setStatus(MaintenanceStatus.RESOLVED);

        Vehicle vehicle = log.getVehicle();

        if(vehicle.getStatus() != VehicleStatus.RETIRED){
            vehicle.setStatus(VehicleStatus.AVAILABLE);
            vehicleRepository.save(vehicle);
        }

        return convert(maintenanceRepository.save(log));
    }

    @Override
    public MaintenanceLogDTO getMaintenanceById(Long id) {

        return convert(
                maintenanceRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"))
        );
    }

    @Override
    public List<MaintenanceLogDTO> getAllMaintenance() {

        return maintenanceRepository.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMaintenance(Long id) {

        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance not found");
        }
        maintenanceRepository.deleteById(id);
    }

    private MaintenanceLogDTO convert(MaintenanceLog log){

        MaintenanceLogDTO dto = new MaintenanceLogDTO();

        dto.setId(log.getId());
        dto.setIssueDescription(log.getIssueDescription());
        dto.setPriority(log.getPriority());
        dto.setTechnicianName(log.getTechnicianName());
        dto.setCost(log.getCost());
        dto.setStatus(log.getStatus());
        dto.setVehicleId(log.getVehicle().getId());

        return dto;
    }
}
