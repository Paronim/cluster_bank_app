package com.donbank.repository;

import com.donbank.config.Config;
import com.donbank.db.DatabaseConnector;
import com.donbank.entity.Account;
import com.donbank.entity.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private Connection connection;
    private Config config;

    @BeforeEach
    void setUp() throws SQLException {

        config = Config.getInstance();
        config.setProperty("db.url", "jdbc:postgresql://localhost:5432/test.app_dbi");
        config.setProperty("logging.param", "console");
        connection = DatabaseConnector.getInstance().getConnection();

        transactionRepository = new TransactionRepository();
        accountRepository = new AccountRepository();

        connection.setAutoCommit(false);
    }

    @AfterEach
    void end() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    void testAddTransaction() throws SQLException {
        double amount = 100.0f;
        String transactionType = "withdraw";
        Timestamp createAt = Timestamp.valueOf(LocalDateTime.now());
        createAt.setNanos(0);
        Account account = accountRepository.addAccount("RUB", 100, 10000, "test");
        transactionRepository.addTransaction(amount, transactionType, createAt, account.getId());
        System.out.println(transactionRepository.getAllTransactionByAccountID(account.getId()));
        Transaction transaction = transactionRepository.getAllTransactionByAccountID(account.getId()).getLast();

        assertEquals(transaction.getAccount().getId(), account.getId());
        assertEquals(transaction.getAmount(), amount);
        assertEquals(String.valueOf(transaction.getTransactionType()), transactionType);
        assertEquals(transaction.getCreatedAt(), createAt);
    }

}