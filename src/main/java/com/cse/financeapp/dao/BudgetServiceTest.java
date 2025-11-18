package com.cse.financeapp.dao;

import com.cse.financeapp.models.Budget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BudgetServiceTest {

    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        budgetService = new BudgetService();
    }

    @Test
    void testAddBudget() {
        Budget b = new Budget(1, 500.0);

        Budget saved = budgetService.addBudget(b);

        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals(1, saved.getCategoryId());
        assertEquals(500.0, saved.getLimitAmount());
    }

    @Test
    void testGetBudgetById() {
        Budget b = new Budget(2, 300.0);
        Budget saved = budgetService.addBudget(b);

        Budget found = budgetService.getBudgetById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(2, found.getCategoryId());
    }

    @Test
    void testUpdateBudget() {
        Budget b = new Budget(3, 250.0);
        Budget saved = budgetService.addBudget(b);

        Budget updated = new Budget(saved.getId(), 3, 400.0);

        Budget result = budgetService.updateBudget(saved.getId(), updated);

        assertNotNull(result);
        assertEquals(400.0, result.getLimitAmount());
    }

    @Test
    void testDeleteBudget() {
        Budget b = new Budget(4, 600.0);
        Budget saved = budgetService.addBudget(b);

        boolean deleted = budgetService.deleteBudget(saved.getId());

        assertTrue(deleted);
        assertNull(budgetService.getBudgetById(saved.getId()));
    }
}
