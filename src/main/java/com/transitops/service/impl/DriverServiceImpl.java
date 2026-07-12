package com.transitops.service.impl;

import com.transitops.dto.DriverDTO;
import com.transitops.entity.Driver;
import com.transitops.enums.DriverStatus;
import com.transitops.exception.BusinessException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.DriverRepository;
import com.transitops.service.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Override
    public DriverDTO createDriver(DriverDTO driverDTO) {

        if (driverRepository.existsByLicenseNumber(driverDTO.getLicenseNumber())) {
            throw new BusinessException("Driver license number already exists");
        }

        Driver driver = new Driver();

        driver.setName(driverDTO.getName());
        driver.setLicenseNumber(driverDTO.getLicenseNumber());
        driver.setLicenseCategory(driverDTO.getLicenseCategory());
        driver.setLicenseExpiryDate(driverDTO.getLicenseExpiryDate());
        driver.setContactNumber(driverDTO.getContactNumber());
        driver.setSafetyScore(driverDTO.getSafetyScore());
        driver.setStatus(DriverStatus.AVAILABLE);

        Driver savedDriver = driverRepository.save(driver);

        log.info("Created driver with license number {}", savedDriver.getLicenseNumber());

        return convertToDTO(savedDriver);
    }

    @Override
    public DriverDTO getDriverById(Long id) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

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
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        driver.setName(driverDTO.getName());
        driver.setLicenseCategory(driverDTO.getLicenseCategory());
        driver.setLicenseExpiryDate(driverDTO.getLicenseExpiryDate());
        driver.setContactNumber(driverDTO.getContactNumber());
        driver.setSafetyScore(driverDTO.getSafetyScore());
        driver.setStatus(driverDTO.getStatus());

        Driver updatedDriver = driverRepository.save(driver);

        log.info("Updated driver id {}", id);

        return convertToDTO(updatedDriver);
    }

    @Override
    public void deleteDriver(Long id) {

        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Driver not found");
        }
        driverRepository.deleteById(id);
        log.info("Deleted driver id {}", id);
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
