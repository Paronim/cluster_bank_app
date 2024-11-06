package com.donbank.service;

import com.donbank.entity.Account;
import com.donbank.entity.Transaction;
import com.donbank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private TransactionService transactionService;

    @BeforeEach
    void setUp(){
        transactionRepository = mock(TransactionRepository.class);
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    void testAddTransaction() {

        assertDoesNotThrow(() ->
                transactionService.addTransaction(100, "withdraw", 10000));

    }

    @Test
    void testGetTransactions() {
        String currency = "USD";
        String name = "test";
        int clientId = 10000;
        Account account = new Account(10000L, Account.Currency.valueOf(currency), 100d, clientId, name);

        AtomicReference<List<Transaction>> result = new AtomicReference<>();

        assertDoesNotThrow(() ->
                result.set(transactionService.getTransactions(account)));

        assertEquals(result.get(), new ArrayList<Transaction>());
    }
}