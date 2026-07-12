package com.transitops.service.impl;

import com.transitops.dto.VehicleDTO;
import com.transitops.entity.Vehicle;
import com.transitops.enums.VehicleStatus;
import com.transitops.repository.VehicleRepository;
import com.transitops.service.VehicleService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {


    private final VehicleRepository vehicleRepository;


    @Override
    public VehicleDTO createVehicle(VehicleDTO dto) {


        Vehicle vehicle = new Vehicle();

        vehicle.setRegistrationNumber(dto.getRegistrationNumber());
        vehicle.setVehicleName(dto.getVehicleName());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setMaxLoadCapacity(dto.getMaxLoadCapacity());
        vehicle.setOdometer(dto.getOdometer());
        vehicle.setAcquisitionCost(dto.getAcquisitionCost());

        vehicle.setStatus(VehicleStatus.AVAILABLE);


        Vehicle saved = vehicleRepository.save(vehicle);


        return convertToDTO(saved);
    }



    @Override
    public VehicleDTO getVehicleById(Long id) {

        Vehicle vehicle =
                vehicleRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Vehicle not found"));


        return convertToDTO(vehicle);
    }



    @Override
    public List<VehicleDTO> getAllVehicles() {

        return vehicleRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    @Override
    public VehicleDTO updateVehicle(Long id, VehicleDTO dto) {


        Vehicle vehicle =
                vehicleRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Vehicle not found"));


        vehicle.setVehicleName(dto.getVehicleName());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setMaxLoadCapacity(dto.getMaxLoadCapacity());
        vehicle.setOdometer(dto.getOdometer());
        vehicle.setAcquisitionCost(dto.getAcquisitionCost());


        Vehicle updated =
                vehicleRepository.save(vehicle);


        return convertToDTO(updated);
    }



    @Override
    public void deleteVehicle(Long id) {

        vehicleRepository.deleteById(id);

    }



    private VehicleDTO convertToDTO(Vehicle vehicle){


        VehicleDTO dto = new VehicleDTO();


        dto.setId(vehicle.getId());
        dto.setRegistrationNumber(vehicle.getRegistrationNumber());
        dto.setVehicleName(vehicle.getVehicleName());
        dto.setVehicleType(vehicle.getVehicleType());
        dto.setMaxLoadCapacity(vehicle.getMaxLoadCapacity());
        dto.setOdometer(vehicle.getOdometer());
        dto.setAcquisitionCost(vehicle.getAcquisitionCost());
        dto.setStatus(vehicle.getStatus());


        return dto;
    }

}