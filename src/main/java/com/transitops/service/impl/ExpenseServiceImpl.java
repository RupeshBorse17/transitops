package com.transitops.service.impl;

import com.transitops.dto.ExpenseDTO;
import com.transitops.entity.Expense;
import com.transitops.entity.Trip;
import com.transitops.entity.Vehicle;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.ExpenseRepository;
import com.transitops.repository.TripRepository;
import com.transitops.repository.VehicleRepository;
import com.transitops.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final VehicleRepository vehicleRepository;
    private final TripRepository tripRepository;

    @Override
    public ExpenseDTO createExpense(ExpenseDTO dto) {
        Expense expense = new Expense();
        apply(dto, expense);
        Expense saved = expenseRepository.save(expense);
        log.info("Created expense id {}", saved.getId());
        return convert(saved);
    }

    @Override
    public ExpenseDTO getExpenseById(Long id) {
        return convert(findExpense(id));
    }

    @Override
    public List<ExpenseDTO> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO dto) {
        Expense expense = findExpense(id);
        apply(dto, expense);
        Expense updated = expenseRepository.save(expense);
        log.info("Updated expense id {}", id);
        return convert(updated);
    }

    @Override
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense not found");
        }
        expenseRepository.deleteById(id);
        log.info("Deleted expense id {}", id);
    }

    private void apply(ExpenseDTO dto, Expense expense) {
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        Trip trip = null;
        if (dto.getTripId() != null) {
            trip = tripRepository.findById(dto.getTripId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        }

        expense.setExpenseType(dto.getExpenseType());
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        expense.setExpenseDate(dto.getExpenseDate() == null ? LocalDate.now() : dto.getExpenseDate());
        expense.setVehicle(vehicle);
        expense.setTrip(trip);
    }

    private Expense findExpense(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }

    private ExpenseDTO convert(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setExpenseType(expense.getExpenseType());
        dto.setAmount(expense.getAmount());
        dto.setDescription(expense.getDescription());
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setVehicleId(expense.getVehicle().getId());
        dto.setTripId(expense.getTrip() == null ? null : expense.getTrip().getId());
        return dto;
    }
}
