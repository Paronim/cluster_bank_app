package com.donbank.service;

import com.donbank.entity.Account;
import com.donbank.entity.Client;
import com.donbank.repository.ClientRepository;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

/**
 * ClientService is responsible for managing client-related operations.
 * This class acts as a service layer, providing methods to interact with client data,
 * such as retrieving client information, adding or deleting clients, and importing client data from CSV files.
 * It acts as an intermediary between the application and the ClientRepository, abstracting database
 * access and allowing for easier client data management.
 */
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Initializes a new instance of ClientService with a specific client ID and associated accounts.
     * This constructor is useful when there is an existing client ID and list of accounts to manage.
     *
     * @param id the unique identifier of the client.
     * @param accounts a list of accounts associated with the client.
     */
    public ClientService(long id, List<Account> accounts) {
        this.clientRepository = new ClientRepository(id, accounts);
    }

    /**
     * Initializes a default instance of ClientService.
     * This constructor creates a new ClientRepository instance without specific client data.
     */
    public ClientService() {
        this.clientRepository = new ClientRepository();
    }

    /**
     * Initializes a new instance of ClientService with a specific ClientRepository instance.
     * This constructor allows dependency injection, which is useful for testing or when using
     * an existing repository instance.
     *
     * @param clientRepository an instance of ClientRepository.
     */
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Retrieves the current client's details as a Client object.
     * This method fetches data from the ClientRepository.
     *
     * @return an instance of Client containing the current client's information.
     */
    public Client getClient() {
        return clientRepository.getClient();
    }

    /**
     * Adds a new client with the specified first name and last name.
     * The client's unique ID is generated and returned after successful insertion in the repository.
     *
     * @param firstName the first name of the client.
     * @param lastName the last name of the client.
     * @return the unique ID of the newly added client.
     * @throws SQLException if a database access error occurs.
     */
    public long addClient(String firstName, String lastName) throws SQLException {
        return clientRepository.addClient(firstName, lastName);
    }

    /**
     * Deletes a client by their unique ID.
     * This method calls the repository to remove client data from the database.
     *
     * @param id the unique identifier of the client to be deleted.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteClient(long id) throws SQLException {
        clientRepository.deleteClient(id);
    }

    /**
     * Imports client data from a specified CSV file, associating each client with a given list of accounts.
     * Reads client data from the CSV, inserts each client into the database, and assigns the list of accounts to each client.
     * This method ensures that each account is properly associated with its respective client after the import.
     *
     * @param accounts a list of accounts to be associated with each imported client.
     * @param path the file path to the CSV file containing client data.
     * @throws SQLException if a database access error occurs or if adding a client fails.
     * @throws FileNotFoundException if the specified CSV file cannot be found.
     */
    public void importCSVClients(List<Account> accounts, String path) throws SQLException, FileNotFoundException {
        long id;
        List<Client> clients = clientRepository.getListClientsFromCSV(accounts, path);

        for (Client client : clients) {
            id = clientRepository.addClient(client.getFirstName(), client.getLastName());

            if (id == 0) {
                throw new SQLException("Failed to add client to the database.");
            }

            List<Account> accountList = client.getAccounts();

            for (Account account : accountList) {
                account.setClientId(id);
            }
        }
    }
}
