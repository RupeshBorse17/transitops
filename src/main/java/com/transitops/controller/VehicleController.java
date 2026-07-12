package com.transitops.controller;


import com.transitops.dto.VehicleDTO;
import com.transitops.service.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Vehicles", description = "Vehicle registry APIs")
public class VehicleController {


    private final VehicleService vehicleService;



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a vehicle")
    public VehicleDTO createVehicle(
            @Valid @RequestBody VehicleDTO dto){

        return vehicleService.createVehicle(dto);
    }



    @GetMapping
    @Operation(summary = "Get all vehicles")
    public List<VehicleDTO> getAllVehicles(){

        return vehicleService.getAllVehicles();
    }



    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by id")
    public VehicleDTO getVehicle(
            @PathVariable Long id){

        return vehicleService.getVehicleById(id);
    }



    @PutMapping("/{id}")
    @Operation(summary = "Update a vehicle")
    public VehicleDTO updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleDTO dto){

        return vehicleService.updateVehicle(id,dto);
    }



    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle")
    public String deleteVehicle(
            @PathVariable Long id){

        vehicleService.deleteVehicle(id);

        return "Vehicle deleted successfully";
    }

}
