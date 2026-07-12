package com.transitops.repository;

import com.transitops.entity.Trip;
import com.transitops.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TripRepository extends JpaRepository<Trip, Long> {

    long countByStatus(TripStatus status);

    long countByCreatedDate(LocalDate createdDate);

    long countByCreatedDateBetween(LocalDate startDate, LocalDate endDate);
}
