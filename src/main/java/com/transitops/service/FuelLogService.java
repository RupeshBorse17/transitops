package com.transitops.service;

import com.transitops.dto.FuelLogDTO;

import java.util.List;

public interface FuelLogService {

    FuelLogDTO createFuelLog(FuelLogDTO dto);

    FuelLogDTO getFuelLogById(Long id);

    List<FuelLogDTO> getAllFuelLogs();

    void deleteFuelLog(Long id);
}