package com.transitops.controller;

import com.transitops.dto.MaintenanceLogDTO;
import com.transitops.service.MaintenanceLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MaintenanceLogController {

    private final MaintenanceLogService maintenanceService;

    @PostMapping
    public MaintenanceLogDTO create(@RequestBody MaintenanceLogDTO dto){

        return maintenanceService.createMaintenance(dto);
    }

    @PutMapping("/approve/{id}")
    public MaintenanceLogDTO approve(@PathVariable Long id){

        return maintenanceService.approveMaintenance(id);
    }

    @PutMapping("/reject/{id}")
    public MaintenanceLogDTO reject(@PathVariable Long id){

        return maintenanceService.rejectMaintenance(id);
    }

    @PutMapping("/resolve/{id}")
    public MaintenanceLogDTO resolve(@PathVariable Long id){

        return maintenanceService.resolveMaintenance(id);
    }

    @GetMapping("/{id}")
    public MaintenanceLogDTO getById(@PathVariable Long id){

        return maintenanceService.getMaintenanceById(id);
    }

    @GetMapping
    public List<MaintenanceLogDTO> getAll(){

        return maintenanceService.getAllMaintenance();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

        maintenanceService.deleteMaintenance(id);
    }
}
