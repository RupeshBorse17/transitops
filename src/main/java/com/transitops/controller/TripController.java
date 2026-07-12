package com.transitops.controller;

import com.transitops.dto.TripDTO;
import com.transitops.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Trips", description = "Trip lifecycle APIs")
public class TripController {

    private final TripService tripService;

    @PostMapping
    @Operation(summary = "Create a draft trip")
    public TripDTO createTrip(@Valid @RequestBody TripDTO dto){

        return tripService.createTrip(dto);
    }

    @GetMapping
    @Operation(summary = "Get all trips")
    public List<TripDTO> getAllTrips(){

        return tripService.getAllTrips();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get trip by id")
    public TripDTO getTrip(@PathVariable Long id){

        return tripService.getTripById(id);
    }

    @PutMapping("/dispatch/{id}")
    @Operation(summary = "Dispatch a trip")
    public TripDTO dispatch(@PathVariable Long id){

        return tripService.dispatchTrip(id);
    }

    @PutMapping("/complete/{id}")
    @Operation(summary = "Complete a trip")
    public TripDTO complete(@PathVariable Long id){

        return tripService.completeTrip(id);
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "Cancel a trip")
    public TripDTO cancel(@PathVariable Long id){

        return tripService.cancelTrip(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a trip")
    public void delete(@PathVariable Long id){

        tripService.deleteTrip(id);
    }
}
