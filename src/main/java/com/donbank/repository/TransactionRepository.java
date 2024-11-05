package com.donbank.repository;

import com.donbank.config.LoggerConfig;
import com.donbank.db.DatabaseConnector;
import com.donbank.entity.Account;
import com.donbank.entity.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The TransactionRepository class is responsible for managing transactions in the database.
 * It provides methods to add transactions and retrieve transaction details associated with specific accounts.
 */
public class TransactionRepository {

    private final Logger logger;
    private final Connection connection;

    /**
     * Initializes a new instance of TransactionRepository and establishes a database connection.
     */
    public TransactionRepository() {
        this.logger = LoggerConfig.getInstance().getLogger();
        this.connection = DatabaseConnector.getInstance().getConnection();
    }

    /**
     * Adds a transaction to the database.
     *
     * @param amount          the amount of the transaction.
     * @param transactionType the type of transaction (e.g., deposit, withdrawal).
     * @param createdAt       the timestamp of when the transaction was created.
     * @param accountID       the ID of the account associated with the transaction.
     * @throws SQLException if a database access error occurs.
     */
    public void addTransaction(double amount, String transactionType, Timestamp createdAt, long accountID) throws SQLException {
        String query = "INSERT INTO app_dbi.transactions (amount, transaction_type, created_at, account_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, amount);
            statement.setString(2, transactionType);
            statement.setTimestamp(3, createdAt);
            statement.setLong(4, accountID);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Transaction added successfully");
                logger.log(Level.INFO, "Transaction added successfully");
            } else {
                logger.log(Level.WARNING, "No transaction added.");
            }
        }
    }

    /**
     * Retrieves all transactions associated with a specific account ID.
     *
     * @param accountID the ID of the account whose transactions are to be retrieved.
     * @return a list of Transaction objects associated with the specified account ID.
     */
    public List<Transaction> getAllTransactionByAccountID(long accountID) {
        List<Transaction> transactionList = new ArrayList<>();
        String query = """
                SELECT t.id AS transaction_id,
                       t.amount,
                       t.transaction_type,
                       t.created_at,
                       t.account_id,
                       a.currency,
                       a.balance,
                       a.client_id
                FROM app_dbi.transactions t
                LEFT JOIN app_dbi.accounts a ON t.account_id = a.id
                WHERE t.account_id = ?
                ORDER BY t.created_at DESC;""";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, accountID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactionList.add(new Transaction.Builder()
                            .setId(resultSet.getLong("transaction_id"))
                            .setTransactionType(Transaction.TransactionType.valueOf(resultSet.getString("transaction_type")))
                            .setAmount(resultSet.getLong("transaction_id"))
                            .setCreatedAt(resultSet.getTimestamp("created_at"))
                            .setAccount(new Account.Builder()
                                    .setId(resultSet.getLong("account_id"))
                                    .setCurrency(Account.Currency.valueOf(resultSet.getString("currency")))
                                    .setBalance(resultSet.getDouble("balance"))
                                    .setClientId(resultSet.getLong("client_id"))
                                    .build()).build());

                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return transactionList;
    }
}
