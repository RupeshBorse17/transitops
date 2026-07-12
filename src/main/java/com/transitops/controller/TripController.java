package com.transitops.controller;

import com.transitops.dto.TripDTO;
import com.transitops.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TripController {

    private final TripService tripService;

    @PostMapping
    public TripDTO createTrip(@RequestBody TripDTO dto){

        return tripService.createTrip(dto);
    }

    @GetMapping
    public List<TripDTO> getAllTrips(){

        return tripService.getAllTrips();
    }

    @GetMapping("/{id}")
    public TripDTO getTrip(@PathVariable Long id){

        return tripService.getTripById(id);
    }

    @PutMapping("/dispatch/{id}")
    public TripDTO dispatch(@PathVariable Long id){

        return tripService.dispatchTrip(id);
    }

    @PutMapping("/complete/{id}")
    public TripDTO complete(@PathVariable Long id){

        return tripService.completeTrip(id);
    }

    @PutMapping("/cancel/{id}")
    public TripDTO cancel(@PathVariable Long id){

        return tripService.cancelTrip(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

        tripService.deleteTrip(id);
    }
}