package com.donbank.service;

import com.donbank.entity.Account;
import com.donbank.entity.Client;
import com.donbank.repository.ClientRepository;

import java.util.List;

/**
 * ClientService is responsible for managing client-related operations.
 * It acts as an intermediary between the client data repository and
 * the application, providing methods to retrieve client information.
 */
public class ClientService {

    private ClientRepository clientRepository;

    /**
     * Initializes a new instance of ClientService with the specified list of accounts.
     * A new ClientRepository is created using the provided accounts.
     *
     * @param accounts a list of accounts associated with the clients.
     */
    public ClientService(List<Account> accounts) {
        this.clientRepository = new ClientRepository(accounts);
    }

    /**
     * Initializes a new instance of ClientService with the specified ClientRepository.
     * This constructor allows for dependency injection of an existing ClientRepository.
     *
     * @param clientRepository an existing instance of ClientRepository.
     */
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Retrieves a client by their unique identifier.
     *
     * @param id the unique identifier of the client to retrieve.
     * @return the Client object associated with the specified ID,
     *         or null if no client is found.
     */
    public Client getClientById(int id) {
        return clientRepository.findById(id);
    }

}
