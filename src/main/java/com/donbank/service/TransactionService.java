package com.donbank.service;

import com.donbank.entity.Account;
import com.donbank.entity.Transaction;
import com.donbank.exception.AccountNotFoundException;
import com.donbank.repository.TransactionRepository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * TransactionService manages operations related to transactions,
 * including adding new transactions and retrieving transaction histories.
 * It interacts with the TransactionRepository to persist and retrieve transaction data.
 */
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Initializes a new instance of TransactionService,
     * creating a new TransactionRepository instance.
     */
    public TransactionService(){
        this.transactionRepository = new TransactionRepository();
    }

    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    /**
     * Adds a new transaction to the specified account.
     *
     * @param amount          the amount of the transaction.
     * @param transactionType the type of the transaction (e.g., "deposit", "withdrawal").
     * @param accountID       the unique identifier of the account associated with the transaction.
     * @throws SQLException if a database access error occurs.
     */
    public void addTransaction(double amount, String transactionType, long accountID) throws SQLException {
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        createdAt.setNanos(0);
        transactionRepository.addTransaction(amount, transactionType, createdAt, accountID);
    }

    /**
     * Retrieves all transactions associated with an account based on the specified currency.
     *
     * @return a list of Transaction objects associated with the specified account.
     * @throws AccountNotFoundException if no account with the specified currency is found.
     */
    public List<Transaction> getTransactions(Account account) throws AccountNotFoundException {

        return transactionRepository.getAllTransactionByAccountID(account.getId());
    }
}
