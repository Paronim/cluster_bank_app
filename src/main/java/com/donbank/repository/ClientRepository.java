package com.donbank.repository;

import com.donbank.config.LoggerConfig;
import com.donbank.db.DatabaseConnector;
import com.donbank.entity.Account;
import com.donbank.entity.Client;
import lombok.Getter;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ClientRepository manages the storage and retrieval of client data.
 * It provides methods to retrieve clients from the database and CSV files.
 */
public class ClientRepository {

    private final Logger logger;
    private final Connection connection;

    @Getter
    private Client client;

    /**
     * Initializes a new instance of ClientRepository with the specified client ID and accounts.
     *
     * @param id       the unique identifier of the client.
     * @param accounts the list of accounts associated with the client.
     */
    public ClientRepository(long id, List<Account> accounts) {
        this.logger = LoggerConfig.getInstance().getLogger();
        this.connection = DatabaseConnector.getInstance().getConnection();
        this.client = getClient(id, accounts);
    }

    /**
     * Finds a client by their unique identifier in the database.
     *
     * @param id       the unique identifier of the client.
     * @param accounts the list of accounts to associate with the client.
     * @return the Client object associated with the specified ID, or null if not found.
     */
    public Client getClient(long id, List<Account> accounts) {
        String query = "SELECT * FROM app_dbi.clients c WHERE c.id = ?";
        Client client = null;

        try (PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.first()) {
                    client = new Client.Builder()
                            .setId(resultSet.getLong("id"))
                            .setFirstName(resultSet.getString("first_name"))
                            .setLastName(resultSet.getString("last_name"))
                            .setAccounts(accounts)
                            .build();

                } else {
                    logger.log(Level.WARNING, "No client found with the specified ID.");
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving client: " + e.getMessage());
        }

        return client;
    }

    /**
     * Adds a new client to the database.
     *
     * @param firstName the first name of the client.
     * @param lastName  the last name of the client.
     * @return the unique ID of the newly created client.
     * @throws SQLException if a database access error occurs.
     */
    public long addClient(String firstName, String lastName) throws SQLException {
        long id = 0;
        String query = "INSERT INTO app_dbi.clients (first_name, last_name) VALUES (?, ?) RETURNING id";
        try (PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            statement.setString(1, firstName);
            statement.setString(2, lastName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.first()) {
                    id = resultSet.getLong("id");
                } else {
                    logger.log(Level.WARNING, "Error creating new client.");
                }
            }
            return id;
        }
    }

    /**
     * Deletes a client from the database based on their unique ID.
     *
     * @param id the unique identifier of the client to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteClient(long id) throws SQLException {
        String query = "DELETE FROM app_dbi.clients c WHERE c.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Deleted client successfully.");
            } else {
                logger.log(Level.WARNING, "No client found with the specified ID.");
            }
        }

    }

    /**
     * Loads client data from a CSV file and associates it with the provided accounts.
     *
     * @param accounts the list of accounts to associate with each client based on their ID.
     * @param path     the file path to the CSV file containing client data.
     * @return a list of Client objects parsed from the CSV file.
     * @throws FileNotFoundException if the CSV file is not found.
     */
    public List<Client> getListClientsFromCSV(List<Account> accounts, String path) throws FileNotFoundException {
        List<Client> clientList = new ArrayList<>();
        File clientsFile = new File(path);
        Scanner reader = new Scanner(clientsFile);

        while (reader.hasNextLine()) {
            String[] data = reader.nextLine().split(",");
            int idClient = Integer.parseInt(data[0]);

            List<Account> filteredAccounts = accounts.stream()
                    .filter(a -> a.getClientId() == idClient)
                    .toList();

            clientList.add(new Client.Builder()
                    .setId(idClient)
                    .setFirstName(data[1])
                    .setLastName(data[2])
                    .setAccounts(filteredAccounts)
                    .build());
        }

        reader.close();
        return clientList;
    }
}
