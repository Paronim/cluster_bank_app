package com.donbank.repository;

import com.donbank.config.LoggerConfig;
import com.donbank.db.DatabaseConnector;
import com.donbank.entity.Account;
import com.donbank.exception.AccountNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AccountRepository manages the storage and retrieval of account data.
 * It provides methods to add, delete, update, and retrieve accounts,
 * both from a database and from a CSV file.
 */
public class AccountRepository {

    private final Logger logger;
    private final Connection connection;

    /**
     * Initializes a new instance of AccountRepository,
     * setting up the database connection and logging configurations.
     */
    public AccountRepository() {
        this.connection = DatabaseConnector.getInstance().getConnection();
        this.logger = LoggerConfig.getInstance().getLogger();
    }

    /**
     * Retrieves a list of accounts associated with a specific client ID.
     *
     * @param idClient the unique identifier of the client whose accounts are to be retrieved.
     * @return a list of Account objects associated with the specified client ID.
     * @throws SQLException if a database access error occurs.
     */
    public List<Account> getAccountsByIdClient(long idClient) throws SQLException {
        List<Account> accountList = new ArrayList<>();
        String query = "SELECT * FROM app_dbi.accounts a WHERE a.client_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, idClient);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            accountList.add(new Account(
                    resultSet.getLong("id"),
                    Account.Currency.valueOf(resultSet.getString("currency")),
                    resultSet.getDouble("balance"),
                    resultSet.getLong("client_id")
            ));
        }

        statement.close();
        resultSet.close();
        return accountList;
    }

    /**
     * Retrieves an account by its unique ID.
     *
     * @param id the unique identifier of the account.
     * @return the Account object if found, otherwise null.
     */
    public Account getAccount(long id) throws AccountNotFoundException {
        Account account = null;
        String query = "SELECT * FROM app_dbi.accounts a WHERE a.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.first()) {
                account = new Account(
                        resultSet.getLong("id"),
                        Account.Currency.valueOf(resultSet.getString("currency")),
                        resultSet.getDouble("balance"),
                        resultSet.getLong("client_id")
                );
            } else {
                logger.log(Level.WARNING, "No account found with the specified ID.");
                throw new AccountNotFoundException();
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return account;
    }

    /**
     * Adds a new account to the database.
     *
     * @param currency the currency of the new account.
     * @param balance  the initial balance of the account.
     * @param clientID the unique identifier of the client associated with this account.
     * @return the Account object created in the database.
     * @throws SQLException if a database access error occurs.
     */
    public Account addAccount(String currency, double balance, long clientID) throws SQLException {
        Account account = null;
        String query = "INSERT INTO app_dbi.accounts (currency, balance, client_id) VALUES (?, ?, ?) RETURNING id, currency, balance, client_id";
        PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        statement.setString(1, currency);
        statement.setDouble(2, balance);
        statement.setLong(3, clientID);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.first()) {
            account = new Account(
                    resultSet.getLong("id"),
                    Account.Currency.valueOf(resultSet.getString("currency")),
                    resultSet.getDouble("balance"),
                    resultSet.getLong("client_id")
            );
        } else {
            logger.log(Level.WARNING, "No account found with the specified ID.");
        }

        statement.close();
        resultSet.close();
        return account;
    }

    /**
     * Deletes an account from the database based on its unique ID.
     *
     * @param id the unique identifier of the account to delete.
     * @throws SQLException              if a database access error occurs.
     * @throws AccountNotFoundException  if no account with the specified ID is found.
     */
    public void deleteAccount(long id) throws SQLException, AccountNotFoundException {
        String query = "DELETE FROM app_dbi.accounts a WHERE a.id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);

        int rowsAffected = statement.executeUpdate();

        if (rowsAffected > 0) {
            logger.log(Level.INFO, "Account deleted successfully");
        } else {
            logger.log(Level.WARNING, "No account found with the specified ID.");
            throw new AccountNotFoundException();
        }

        statement.close();
    }

    /**
     * Updates an existing account in the database.
     *
     * @param id       the unique identifier of the account to update.
     * @param currency the new currency for the account.
     * @param balance  the updated balance for the account.
     * @param clientID the unique identifier of the client associated with this account.
     * @throws SQLException if a database access error occurs.
     */
    public void updateAccount(long id, String currency, double balance, long clientID) throws SQLException, AccountNotFoundException {
        String query = "UPDATE app_dbi.accounts SET currency = ?, balance = ?, client_id = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, currency);
        statement.setDouble(2, balance);
        statement.setLong(3, clientID);
        statement.setLong(4, id);

        int rowsAffected = statement.executeUpdate();

        if (rowsAffected > 0) {
            logger.log(Level.INFO, "Account updated successfully");
        } else {
            logger.log(Level.SEVERE, "No account found with the specified ID.");
            throw new AccountNotFoundException();
        }

        statement.close();
    }

    /**
     * Loads a list of accounts from a CSV file.
     *
     * @param path the file path to the CSV file containing account data.
     * @return a list of Account objects parsed from the CSV file.
     * @throws FileNotFoundException if the file at the specified path is not found.
     */
    public List<Account> getListAccountsFromCSV(String path) throws FileNotFoundException {
        List<Account> accountList = new ArrayList<>();
        File accountsFile = new File(path);
        Scanner reader = new Scanner(accountsFile);

        while (reader.hasNextLine()) {
            String[] data = reader.nextLine().split(",");
            accountList.add(new Account(
                    Integer.parseInt(data[0]),
                    Account.Currency.valueOf(data[1]),
                    Double.parseDouble(data[2]),
                    Integer.parseInt(data[3])
            ));
        }
        reader.close();

        return accountList;
    }
}
