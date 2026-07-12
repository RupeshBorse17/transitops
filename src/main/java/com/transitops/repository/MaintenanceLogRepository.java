package com.transitops.repository;

import com.transitops.entity.MaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long> {

    @Query("select coalesce(sum(m.cost), 0) from MaintenanceLog m")
    double getTotalMaintenanceCost();
}
