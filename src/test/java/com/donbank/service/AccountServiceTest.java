package com.donbank.service;

import com.donbank.entity.Account;
import com.donbank.exception.AccountNotFoundException;
import com.donbank.exception.InsufficientFundsException;
import com.donbank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    void createAccount() {
        List<Account> accounts = new ArrayList<>();
        String currency = "USD";
        int clientId = 1;

        when(accountRepository.getAccountsData()).thenReturn(accounts);

        String result = accountService.createAccount(currency, accounts, clientId);

        assertEquals("account created", result);
        assertEquals(1, accounts.size());
        assertEquals("USD", accounts.getFirst().getCurrency());
    }

    @Test
    public void testCreateAccount_InvalidCurrency() {
        List<Account> accounts = new ArrayList<>();
        String currency = "EUR";
        int clientId = 1;

        String result = accountService.createAccount(currency, accounts, clientId);

        assertEquals("Not valid currency", result);
        assertTrue(accounts.isEmpty());
    }

    @Test
    public void testCreateAccount_AccountAlreadyExists() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(1, "USD", 0.0, 1));
        String currency = "USD";
        int clientId = 1;

        String result = accountService.createAccount(currency, accounts, clientId);

        assertEquals("an account with this currency already exists", result);
        assertEquals(1, accounts.size());
    }

    @Test
    void testDepositFunds_Success() throws InsufficientFundsException {
        List<Account> accounts = new ArrayList<>();
        Account account = new Account(1, "USD", 100.0, 1);
        accounts.add(account);
        String currency = "USD";
        int amount = 50;
        String param = "contribute";

        String result = accountService.depositFunds(currency, amount, accounts, param);

        assertEquals("Amount updated", result);
        assertEquals(50.0, account.getBalance());
    }

    @Test
    void testDepositFunds_InsufficientFunds() {
        List<Account> accounts = new ArrayList<>();
        Account account = new Account(1, "USD", 20.0, 1);
        accounts.add(account);
        String currency = "USD";
        int amount = 50;
        String param = "contribute";

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.depositFunds(currency, amount, accounts, param);
        });
    }

    @Test
    void testDepositFunds_InvalidCurrency() throws InsufficientFundsException {
        List<Account> accounts = new ArrayList<>();
        Account account = new Account(1, "USD", 100.0, 1);
        accounts.add(account);
        String currency = "EUR";
        int amount = 50;
        String param = "contribute";


        String result = accountService.depositFunds(currency, amount, accounts, param);
        assertEquals("Not valid currency", result);

    }

    @Test
    void testDepositFunds_AddFunds() throws InsufficientFundsException {
        List<Account> accounts = new ArrayList<>();
        Account account = new Account(1, "USD", 100.0, 1);
        accounts.add(account);
        String currency = "USD";
        int amount = 50;
        String param = "add";

        String result = accountService.depositFunds(currency, amount, accounts, param);

        assertEquals("Amount updated", result);
        assertEquals(150.0, account.getBalance());
    }

    @Test
    public void testDeleteAccount_Success() throws AccountNotFoundException {
        List<Account> accounts = new ArrayList<>();
        Account account = new Account(1, "USD", 100.0, 1);
        accounts.add(account);

        String result = accountService.deleteAccount("USD", accounts);

        assertEquals("Account removed", result);
        assertTrue(accounts.isEmpty());
    }

    @Test
    public void testDeleteAccount_NotValidCurrency() throws AccountNotFoundException {
        List<Account> accounts = new ArrayList<>();
        String result = accountService.deleteAccount("EUR", accounts);

        assertEquals("Not valid currency", result);
    }

    @Test
    public void testDeleteAccount_AccountNotFound() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(1, "USD", 100.0, 1));

        Exception exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.deleteAccount("RUB", accounts);
        });

        assertNotNull(exception);
    }
}