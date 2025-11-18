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
        Budget b = new Budget(1, 500.0);

        // Mock Supabase insert response
        Mockito.when(client.insert(Mockito.eq("budget"), Mockito.anyString()))
                .thenReturn("{\"status\":\"success\"}");

        service.addBudget(b);

        Mockito.verify(client).insert(Mockito.eq("budget"), Mockito.anyString());
    }
}
