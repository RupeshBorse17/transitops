package com.transitops.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExpenseDTO {

    private Long id;

    @NotBlank(message = "Expense type is required")
    private String expenseType;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    private String description;

    private LocalDate expenseDate;

    @NotNull(message = "Vehicle id is required")
    private Long vehicleId;

    private Long tripId;
}
