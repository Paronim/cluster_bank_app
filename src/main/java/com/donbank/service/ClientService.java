package com.donbank.service;

import com.donbank.entity.Account;
import com.donbank.entity.Client;
import com.donbank.repository.ClientRepository;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

/**
 * ClientService is responsible for managing client-related operations.
 * It serves as an intermediary between the client data repository and
 * the application, providing methods to retrieve and import client information.
 */
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Initializes a new instance of ClientService with the specified client ID and list of accounts.
     * A new ClientRepository is created using the provided client ID and associated accounts.
     *
     * @param id the unique identifier of the client.
     * @param accounts a list of accounts associated with the client.
     */
    public ClientService(long id, List<Account> accounts) {
        this.clientRepository = new ClientRepository(id, accounts);
    }

    /**
     * Initializes a new instance of ClientService with the specified ClientRepository.
     * This constructor allows for dependency injection of an existing ClientRepository instance.
     *
     * @param clientRepository an existing instance of ClientRepository.
     */
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Retrieves the current client details.
     *
     * @return an instance of Client containing client information.
     */
    public Client getClient() {
        return clientRepository.getClient();
    }

    /**
     * Imports client data from a CSV file and assigns accounts to clients.
     * This method reads a list of clients from the specified CSV path,
     * adds them to the database, and associates the given accounts.
     *
     * @param accounts a list of accounts to be associated with imported clients.
     * @param path the file path to the CSV file containing client data.
     * @throws SQLException if a database access error occurs or if client insertion fails.
     * @throws FileNotFoundException if the specified CSV file is not found.
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
