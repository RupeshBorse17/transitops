package com.transitops.service.impl;

import com.transitops.dto.TripDTO;
import com.transitops.entity.Driver;
import com.transitops.entity.Trip;
import com.transitops.entity.Vehicle;
import com.transitops.enums.DriverStatus;
import com.transitops.enums.TripStatus;
import com.transitops.enums.VehicleStatus;
import com.transitops.exception.BusinessException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.DriverRepository;
import com.transitops.repository.TripRepository;
import com.transitops.repository.VehicleRepository;
import com.transitops.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    @Override
    public TripDTO createTrip(TripDTO dto) {

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        if(vehicle.getStatus()!=VehicleStatus.AVAILABLE)
            throw new BusinessException("Vehicle is not available for dispatch");

        if(driver.getStatus()!= DriverStatus.AVAILABLE)
            throw new BusinessException("Driver is not available for dispatch");

        if(driver.getLicenseExpiryDate().isBefore(LocalDate.now()) || driver.getLicenseExpiryDate().isEqual(LocalDate.now()))
            throw new BusinessException("Driver license is expired");

        if(dto.getCargoWeight() > vehicle.getMaxLoadCapacity())
            throw new BusinessException("Cargo weight exceeds vehicle maximum load capacity");


        Trip trip=new Trip();

        trip.setSource(dto.getSource());
        trip.setDestination(dto.getDestination());
        trip.setCargoWeight(dto.getCargoWeight());
        trip.setPlannedDistance(dto.getPlannedDistance());
        trip.setStatus(TripStatus.DRAFT);
        trip.setVehicle(vehicle);
        trip.setDriver(driver);

        Trip saved = tripRepository.save(trip);
        log.info("Created draft trip id {}", saved.getId());
        return convert(saved);
    }

    @Override
    public TripDTO dispatchTrip(Long id) {

        Trip trip=tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        if (trip.getStatus() != TripStatus.DRAFT) {
            throw new BusinessException("Only draft trips can be dispatched");
        }
        if (trip.getVehicle().getStatus() != VehicleStatus.AVAILABLE) {
            throw new BusinessException("Vehicle is not available for dispatch");
        }
        if (trip.getDriver().getStatus() != DriverStatus.AVAILABLE) {
            throw new BusinessException("Driver is not available for dispatch");
        }
        if (trip.getDriver().getLicenseExpiryDate().isBefore(LocalDate.now()) || trip.getDriver().getLicenseExpiryDate().isEqual(LocalDate.now())) {
            throw new BusinessException("Driver license is expired");
        }
        if (trip.getCargoWeight() > trip.getVehicle().getMaxLoadCapacity()) {
            throw new BusinessException("Cargo weight exceeds vehicle maximum load capacity");
        }

        trip.setStatus(TripStatus.DISPATCHED);

        trip.getVehicle().setStatus(VehicleStatus.ON_TRIP);
        trip.getDriver().setStatus(DriverStatus.ON_TRIP);

        vehicleRepository.save(trip.getVehicle());
        driverRepository.save(trip.getDriver());

        Trip saved = tripRepository.save(trip);
        log.info("Dispatched trip id {}", id);
        return convert(saved);
    }

    @Override
    public TripDTO completeTrip(Long id) {

        Trip trip=tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        if (trip.getStatus() != TripStatus.DISPATCHED) {
            throw new BusinessException("Only dispatched trips can be completed");
        }

        trip.setStatus(TripStatus.COMPLETED);

        trip.getVehicle().setStatus(VehicleStatus.AVAILABLE);
        trip.getDriver().setStatus(DriverStatus.AVAILABLE);

        vehicleRepository.save(trip.getVehicle());
        driverRepository.save(trip.getDriver());

        Trip saved = tripRepository.save(trip);
        log.info("Completed trip id {}", id);
        return convert(saved);
    }

    @Override
    public TripDTO cancelTrip(Long id) {

        Trip trip=tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        if (trip.getStatus() == TripStatus.COMPLETED) {
            throw new BusinessException("Completed trips cannot be cancelled");
        }

        trip.setStatus(TripStatus.CANCELLED);

        trip.getVehicle().setStatus(VehicleStatus.AVAILABLE);
        trip.getDriver().setStatus(DriverStatus.AVAILABLE);

        vehicleRepository.save(trip.getVehicle());
        driverRepository.save(trip.getDriver());

        Trip saved = tripRepository.save(trip);
        log.info("Cancelled trip id {}", id);
        return convert(saved);
    }

    @Override
    public TripDTO getTripById(Long id) {

        return convert(tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found")));
    }

    @Override
    public List<TripDTO> getAllTrips() {

        return tripRepository.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTrip(Long id) {

        if (!tripRepository.existsById(id)) {
            throw new ResourceNotFoundException("Trip not found");
        }
        tripRepository.deleteById(id);
        log.info("Deleted trip id {}", id);
    }

    private TripDTO convert(Trip trip){

        TripDTO dto=new TripDTO();

        dto.setId(trip.getId());
        dto.setSource(trip.getSource());
        dto.setDestination(trip.getDestination());
        dto.setCargoWeight(trip.getCargoWeight());
        dto.setPlannedDistance(trip.getPlannedDistance());
        dto.setStatus(trip.getStatus());
        dto.setVehicleId(trip.getVehicle().getId());
        dto.setDriverId(trip.getDriver().getId());

        return dto;
    }

}
