package com.transitops.service;

import com.transitops.dto.MaintenanceLogDTO;

import java.util.List;

public interface MaintenanceLogService {

    MaintenanceLogDTO createMaintenance(MaintenanceLogDTO dto);

    MaintenanceLogDTO approveMaintenance(Long id);

    MaintenanceLogDTO rejectMaintenance(Long id);

    MaintenanceLogDTO resolveMaintenance(Long id);

    MaintenanceLogDTO getMaintenanceById(Long id);

    List<MaintenanceLogDTO> getAllMaintenance();

    void deleteMaintenance(Long id);
}