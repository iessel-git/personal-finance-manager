package com.cse.financeapp.dao;

import com.cse.financeapp.models.Budget;
import com.cse.financeapp.service.SupabaseClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class BudgetServiceTest {

    private SupabaseClient client;
    private BudgetService service;

    @BeforeEach
    void setup() {
        client = Mockito.mock(SupabaseClient.class);
        service = new BudgetService(client);
    }

    @Test
    void testAddBudget() throws Exception {
        // Budget(categoryId, limitAmount)
        Budget b = new Budget(5, 500.0);

        // Mock response EXACTLY as BudgetService expects
        String mockResponse = "[{\"id\": 10, \"category_id\": 5, \"limit_amount\": 500.0}]";

        Mockito.when(client.insert(Mockito.eq("budget"), Mockito.anyString()))
                .thenReturn(mockResponse);

        Budget saved = service.addBudget(b);

        // Assertions
        assertNotNull(saved);
        assertEquals(10, saved.getId());
        assertEquals(5, saved.getCategoryId());
        assertEquals(500.0, saved.getLimitAmount());

        // Verify Supabase call
        Mockito.verify(client).insert(Mockito.eq("budget"), Mockito.anyString());
    }
}
