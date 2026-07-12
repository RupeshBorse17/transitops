package com.transitops.service;

import com.transitops.dto.TripDTO;

import java.util.List;

public interface TripService {

    TripDTO createTrip(TripDTO tripDTO);

    TripDTO getTripById(Long id);

    List<TripDTO> getAllTrips();

    TripDTO dispatchTrip(Long id);

    TripDTO completeTrip(Long id);

    TripDTO cancelTrip(Long id);

    void deleteTrip(Long id);
}