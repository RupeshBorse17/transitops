package com.transitops.repository;

import com.transitops.entity.FuelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FuelLogRepository extends JpaRepository<FuelLog,Long> {

    @Query("select coalesce(sum(f.cost), 0) from FuelLog f")
    double getTotalFuelCost();

    @Query("select coalesce(sum(f.liters), 0) from FuelLog f")
    double getTotalFuelLiters();
}
