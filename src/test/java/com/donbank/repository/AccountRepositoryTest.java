package com.donbank.repository;

import com.donbank.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryTest {
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository = new AccountRepository();
    }

    @Test
    void testGetAccountsByIdClient() {
        int testClientId = 1;
        List<Account> accounts = accountRepository.getAccountsByIdClient(testClientId);

        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());

        accounts.forEach(account -> assertEquals(testClientId, account.getClientId()));
    }

    @Test
    void testGetAccountsData() {
        List<Account> accounts = accountRepository.getAccountsData();

        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
    }

    @Test
    void testGetListAccounts_FileNotFound() {
        String invalidFilePath = "src/test/resources/invalid_accounts.csv";

        File tempFile = new File(invalidFilePath);

        assertFalse(tempFile.exists());

        AccountRepository invalidAccountRepository = new AccountRepository(invalidFilePath);

        List<Account> accounts = invalidAccountRepository.getAccountsData();
        assertNotNull(accounts);
        assertTrue(accounts.isEmpty());
    }

    @Test
    void testGetListAccounts_Success() throws IOException {
        String testFilePath = "src/test/resources/test_accounts.csv";
        File testFile = new File(testFilePath);
        FileWriter writer = new FileWriter(testFile);

        writer.write("1,USD,100.0,1\n");
        writer.write("2,EUR,200.0,1\n");
        writer.write("3,USD,150.0,2\n");
        writer.close();

        AccountRepository testAccountRepository = new AccountRepository(testFilePath);

        List<Account> accounts = testAccountRepository.getAccountsData();
        assertNotNull(accounts);
        assertEquals(3, accounts.size());

        testFile.delete();
    }
}