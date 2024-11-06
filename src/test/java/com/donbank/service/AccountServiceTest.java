package com.donbank.service;

import com.donbank.config.Config;
import com.donbank.entity.Account;
import com.donbank.exception.AccountNotFoundException;
import com.donbank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp(){
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService(accountRepository);
    }

    @Test
    void testCreateAccount() {
        List<Account> accounts = new ArrayList<>();
        String currency = "USD";
        int clientId = 10000;
        AtomicReference<String> result = new AtomicReference<>();
        
        assertDoesNotThrow(() -> {
            result.set(accountService.createAccount(currency, accounts, clientId));});

        assertEquals("Account created", result.get());
        
    }

    @Test
    void testDepositFunds() throws AccountNotFoundException {
        String currency = "USD";
        double amount = 100f;
        String name = "test";
        String param = "withdraw";
        int clientId = 10000;
        Account account = new Account(10000L, Account.Currency.valueOf(currency), 100d, clientId, name);

        AtomicReference<String> result = new AtomicReference<>();

        when(accountRepository.getAccount(account.getId())).thenReturn(account);

        assertDoesNotThrow(() -> {
            result.set(accountService.depositFunds(account, amount, param));});

        assertEquals("Amount updated", result.get());

    }

    @Test
    void testDeleteAccount(){
        String currency = "USD";
        String name = "test";
        int clientId = 10000;
        Account account = new Account(10000L, Account.Currency.valueOf(currency), 100d, clientId, name);

        assertDoesNotThrow(() ->
                accountService.deleteAccount(account));

    }

    @Test
    void testGetAccountsByCurrency(){
        List<Account> accounts = new ArrayList<>();
        String currency = "USD";
        String name = "test";
        int clientId = 10000;
        Account account = new Account(10000L, Account.Currency.valueOf(currency), 100d, clientId, name);
        accounts.add(account);

        AtomicReference<Account> result = new AtomicReference<>();

        assertDoesNotThrow(() ->
                result.set(accountService.getAccountsByCurrency(currency, accounts)));

        assertEquals(result.get().getId(), 10000L);
        assertEquals(String.valueOf(result.get().getCurrency()), currency);
        assertEquals(result.get().getBalance(), 100d);
        assertEquals(result.get().getClientId(), 10000L);

    }

    @Test
    void testGetListAccountsCSV(){
        AtomicReference<List<Account>> result = new AtomicReference<>();

        assertDoesNotThrow(() ->
                result.set(accountService.getListAccountsCSV(Config.getInstance().getProperty("csv.accounts"))));

        assertEquals(new ArrayList<Account>(), result.get());
    }

    @Test
    void testImportCSVAccounts(){
        List<Account> accounts = new ArrayList<>();
        String currency = "USD";
        String name = "test";
        int clientId = 10000;
        Account account = new Account(10000L, Account.Currency.valueOf(currency), 100d, clientId, name);
        accounts.add(account);

        assertDoesNotThrow(() ->
                accountService.importCSVAccounts(accounts));

    }
}