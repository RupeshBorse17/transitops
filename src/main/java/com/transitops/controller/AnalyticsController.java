package com.transitops.controller;

import com.transitops.dto.AnalyticsDTO;
import com.transitops.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Analytics", description = "Fleet analytics APIs")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    @Operation(summary = "Get fleet analytics")
    public AnalyticsDTO getAnalytics() {
        return analyticsService.getAnalytics();
    }
}
