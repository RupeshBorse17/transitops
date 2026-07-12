package com.transitops.controller;

import com.transitops.dto.ExpenseDTO;
import com.transitops.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Expenses", description = "Fuel, toll, maintenance and operational expense APIs")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an expense")
    public ExpenseDTO createExpense(@Valid @RequestBody ExpenseDTO dto) {
        return expenseService.createExpense(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get expense by id")
    public ExpenseDTO getExpense(@PathVariable Long id) {
        return expenseService.getExpenseById(id);
    }

    @GetMapping
    @Operation(summary = "Get all expenses")
    public List<ExpenseDTO> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an expense")
    public ExpenseDTO updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseDTO dto) {
        return expenseService.updateExpense(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an expense")
    public void deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
    }
}
