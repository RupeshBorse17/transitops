package com.transitops.controller;

import com.transitops.dto.MaintenanceLogDTO;
import com.transitops.service.MaintenanceLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Maintenance", description = "Vehicle maintenance workflow APIs")
public class MaintenanceLogController {

    private final MaintenanceLogService maintenanceService;

    @PostMapping
    @Operation(summary = "Create a maintenance log")
    public MaintenanceLogDTO create(@Valid @RequestBody MaintenanceLogDTO dto){

        return maintenanceService.createMaintenance(dto);
    }

    @PutMapping("/approve/{id}")
    @Operation(summary = "Approve maintenance and move vehicle to shop")
    public MaintenanceLogDTO approve(@PathVariable Long id){

        return maintenanceService.approveMaintenance(id);
    }

    @PutMapping("/reject/{id}")
    @Operation(summary = "Reject maintenance")
    public MaintenanceLogDTO reject(@PathVariable Long id){

        return maintenanceService.rejectMaintenance(id);
    }

    @PutMapping("/resolve/{id}")
    @Operation(summary = "Resolve maintenance and restore vehicle")
    public MaintenanceLogDTO resolve(@PathVariable Long id){

        return maintenanceService.resolveMaintenance(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get maintenance log by id")
    public MaintenanceLogDTO getById(@PathVariable Long id){

        return maintenanceService.getMaintenanceById(id);
    }

    @GetMapping
    @Operation(summary = "Get all maintenance logs")
    public List<MaintenanceLogDTO> getAll(){

        return maintenanceService.getAllMaintenance();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete maintenance log")
    public void delete(@PathVariable Long id){

        maintenanceService.deleteMaintenance(id);
    }
}
