package com.transitops.controller;

import com.transitops.dto.FuelLogDTO;
import com.transitops.service.FuelLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuel")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Fuel Logs", description = "Vehicle fuel log APIs")
public class FuelLogController {

    private final FuelLogService fuelLogService;

    @PostMapping
    @Operation(summary = "Create a fuel log")
    public FuelLogDTO create(@Valid @RequestBody FuelLogDTO dto) {

        return fuelLogService.createFuelLog(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get fuel log by id")
    public FuelLogDTO getById(@PathVariable Long id) {

        return fuelLogService.getFuelLogById(id);
    }

    @GetMapping
    @Operation(summary = "Get all fuel logs")
    public List<FuelLogDTO> getAll() {

        return fuelLogService.getAllFuelLogs();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete fuel log")
    public void delete(@PathVariable Long id) {

        fuelLogService.deleteFuelLog(id);
    }
}
