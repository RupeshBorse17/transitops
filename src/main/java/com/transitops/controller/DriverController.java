package com.transitops.controller;

import com.transitops.dto.DriverDTO;
import com.transitops.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Drivers", description = "Driver management APIs")
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    @Operation(summary = "Create a driver")
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) {
        return driverService.createDriver(driverDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get driver by id")
    public DriverDTO getDriver(@PathVariable Long id) {
        return driverService.getDriverById(id);
    }

    @GetMapping
    @Operation(summary = "Get all drivers")
    public List<DriverDTO> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a driver")
    public DriverDTO updateDriver(@PathVariable Long id,
                                  @Valid @RequestBody DriverDTO driverDTO) {
        return driverService.updateDriver(id, driverDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a driver")
    public void deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
    }
}
