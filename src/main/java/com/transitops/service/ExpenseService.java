package com.transitops.service;

import com.transitops.dto.ExpenseDTO;

import java.util.List;

public interface ExpenseService {

    ExpenseDTO createExpense(ExpenseDTO dto);

    ExpenseDTO getExpenseById(Long id);

    List<ExpenseDTO> getAllExpenses();

    ExpenseDTO updateExpense(Long id, ExpenseDTO dto);

    void deleteExpense(Long id);
}
