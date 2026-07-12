package com.transitops.service.impl;

import com.transitops.dto.DriverDTO;
import com.transitops.entity.Driver;
import com.transitops.enums.DriverStatus;
import com.transitops.repository.DriverRepository;
import com.transitops.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Override
    public DriverDTO createDriver(DriverDTO driverDTO) {

        Driver driver = new Driver();

        driver.setName(driverDTO.getName());
        driver.setLicenseNumber(driverDTO.getLicenseNumber());
        driver.setLicenseCategory(driverDTO.getLicenseCategory());
        driver.setLicenseExpiryDate(driverDTO.getLicenseExpiryDate());
        driver.setContactNumber(driverDTO.getContactNumber());
        driver.setSafetyScore(driverDTO.getSafetyScore());
        driver.setStatus(DriverStatus.AVAILABLE);

        Driver savedDriver = driverRepository.save(driver);

        return convertToDTO(savedDriver);
    }

    @Override
    public DriverDTO getDriverById(Long id) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        return convertToDTO(driver);
    }

    @Override
    public List<DriverDTO> getAllDrivers() {

        return driverRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DriverDTO updateDriver(Long id, DriverDTO driverDTO) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        driver.setName(driverDTO.getName());
        driver.setLicenseCategory(driverDTO.getLicenseCategory());
        driver.setLicenseExpiryDate(driverDTO.getLicenseExpiryDate());
        driver.setContactNumber(driverDTO.getContactNumber());
        driver.setSafetyScore(driverDTO.getSafetyScore());
        driver.setStatus(driverDTO.getStatus());

        Driver updatedDriver = driverRepository.save(driver);

        return convertToDTO(updatedDriver);
    }

    @Override
    public void deleteDriver(Long id) {

        driverRepository.deleteById(id);
    }

    private DriverDTO convertToDTO(Driver driver) {

        DriverDTO dto = new DriverDTO();

        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setLicenseNumber(driver.getLicenseNumber());
        dto.setLicenseCategory(driver.getLicenseCategory());
        dto.setLicenseExpiryDate(driver.getLicenseExpiryDate());
        dto.setContactNumber(driver.getContactNumber());
        dto.setSafetyScore(driver.getSafetyScore());
        dto.setStatus(driver.getStatus());

        return dto;
    }
}