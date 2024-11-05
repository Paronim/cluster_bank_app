package com.donbank.repository;

import com.donbank.config.Config;
import com.donbank.db.DatabaseConnector;
import com.donbank.entity.Account;
import com.donbank.exception.AccountNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryTest{
    private AccountRepository accountRepository;
    private Connection connection;
    private Config config;

    @BeforeEach
    void setUp() throws SQLException {

        config = Config.getInstance();
        config.setProperty("db.url", "jdbc:postgresql://localhost:5432/test.app_dbi");
        config.setProperty("logging.param", "console");
        connection = DatabaseConnector.getInstance().getConnection();

        accountRepository = new AccountRepository();

        connection.setAutoCommit(false);
    }

    @AfterEach
    void end() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    void testGetAccountsByIdClient() throws SQLException {

        accountRepository.addAccount("RUB", 0, 10000L);

        List<Account> accounts = accountRepository.getAccountsByIdClient(10000L);

        assertEquals(accounts.getFirst().getClientId(), 10000L);
        assertEquals(String.valueOf(accounts.getFirst().getCurrency()), "RUB");
        assertEquals(accounts.getFirst().getBalance(), 0.0);

    }

    @Test
    void testGetAccount() throws SQLException, AccountNotFoundException {
        long id = accountRepository.addAccount("RUB", 0, 10000L).getId();

        Account account = accountRepository.getAccount(id);

        assertEquals(account.getClientId(), 10000L);
        assertEquals(String.valueOf(account.getCurrency()), "RUB");
        assertEquals(account.getBalance(), 0.0);
    }

    @Test
    void testAddAccount() throws SQLException, AccountNotFoundException {
        long id = accountRepository.addAccount("RUB", 10, 10000L).getId();

        Account account = accountRepository.getAccount(id);

        assertEquals(account.getClientId(), 10000L);
        assertEquals(String.valueOf(account.getCurrency()), "RUB");
        assertEquals(account.getBalance(), 10.0);
    }

    @Test
    void testDeleteAccount() throws SQLException {
        long id = accountRepository.addAccount("RUB", 10, 10000L).getId();

        assertDoesNotThrow(() ->
            accountRepository.deleteAccount(id)
        );

        assertThrows(AccountNotFoundException.class, () ->
            accountRepository.getAccount(id)
        );
    }

    @Test
    void testUpdateAccount() throws SQLException, AccountNotFoundException {
        long id = accountRepository.addAccount("RUB", 10, 10000L).getId();

        assertDoesNotThrow(() -> {
            accountRepository.updateAccount(id, "RUB", 1000, 10000L);
        });

        Account account = accountRepository.getAccount(id);

        assertEquals(account.getClientId(), 10000L);
        assertEquals(String.valueOf(account.getCurrency()), "RUB");
        assertEquals(account.getBalance(), 1000.0);
    }

    @Test
    void testListAccountsFromCSV_FileNotFound() throws FileNotFoundException {
        String invalidFilePath = "src/test/resources/invalid_accounts.csv";

        File tempFile = new File(invalidFilePath);

        assertFalse(tempFile.exists());

        assertThrows(FileNotFoundException.class, () -> accountRepository.getListAccountsFromCSV(invalidFilePath));

    }

    @Test
    void testGetListAccountsFromCSV_Success() throws IOException {
        String testFilePath = "src/test/resources/test_accounts.csv";
        File testFile = new File(testFilePath);
        FileWriter writer = new FileWriter(testFile);

        writer.write("1,USD,100.0,1\n");
        writer.write("2,RUB,200.0,1\n");
        writer.write("3,USD,150.0,2\n");
        writer.close();

        List<Account> accounts = accountRepository.getListAccountsFromCSV(testFilePath);
        assertNotNull(accounts);
        assertEquals(3, accounts.size());

        testFile.delete();
    }

}
