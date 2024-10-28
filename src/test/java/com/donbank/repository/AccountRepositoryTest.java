package com.donbank.repository;

import com.donbank.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for verifying the functionality of {@link AccountRepository}.
 *
 * <p>
 * This class includes tests for retrieving accounts by client ID,
 * checking the list of accounts, and handling file-related scenarios.
 * </p>
 */
class AccountRepositoryTest {
    private AccountRepository accountRepository;

    /**
     * Method executed before each test, which initializes the AccountRepository instance.
     */
    @BeforeEach
    void setUp() {
        accountRepository = new AccountRepository();
    }

    /**
     * Tests retrieving accounts by client ID.
     *
     * <p>
     * This test verifies that the accounts retrieved for a valid client ID
     * are not null, not empty, and that each account belongs to the specified client.
     * </p>
     */
    @Test
    void testGetAccountsByIdClient() {
        int testClientId = 1;
        List<Account> accounts = accountRepository.getAccountsByIdClient(testClientId);

        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());

        accounts.forEach(account -> assertEquals(testClientId, account.getClientId()));
    }

    /**
     * Tests retrieving the list of accounts.
     *
     * <p>
     * This test checks that the account data is retrieved successfully,
     * ensuring the list is not null and not empty.
     * </p>
     */
    @Test
    void testGetAccountsData() {
        List<Account> accounts = accountRepository.getAccountsData();

        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
    }

    /**
     * Tests retrieving the list of accounts when the file is not found.
     *
     * <p>
     * This test ensures that when an invalid file path is provided,
     * the method returns an empty list of accounts.
     * </p>
     */
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

    /**
     * Tests retrieving the list of accounts successfully.
     *
     * <p>
     * This test checks that accounts are correctly read from a valid CSV file,
     * ensuring the size of the list matches the expected count.
     * </p>
     *
     * @throws IOException If an error occurs while writing the test file.
     */
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
