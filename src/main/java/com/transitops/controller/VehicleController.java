package com.transitops.controller;


import com.transitops.dto.VehicleDTO;
import com.transitops.service.VehicleService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@CrossOrigin("*")
public class VehicleController {


    private final VehicleService vehicleService;



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleDTO createVehicle(
            @RequestBody VehicleDTO dto){

        return vehicleService.createVehicle(dto);
    }



    @GetMapping
    public List<VehicleDTO> getAllVehicles(){

        return vehicleService.getAllVehicles();
    }



    @GetMapping("/{id}")
    public VehicleDTO getVehicle(
            @PathVariable Long id){

        return vehicleService.getVehicleById(id);
    }



    @PutMapping("/{id}")
    public VehicleDTO updateVehicle(
            @PathVariable Long id,
            @RequestBody VehicleDTO dto){

        return vehicleService.updateVehicle(id,dto);
    }



    @DeleteMapping("/{id}")
    public String deleteVehicle(
            @PathVariable Long id){

        vehicleService.deleteVehicle(id);

        return "Vehicle deleted successfully";
    }

}