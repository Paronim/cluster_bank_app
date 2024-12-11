package com.don.bank.service;

import com.don.bank.dto.ClientDTO;
import com.don.bank.entity.Account;
import com.don.bank.entity.Client;
import com.don.bank.repository.ClientRepository;
import com.don.bank.util.mappingUtils.MappingUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing clients. Provides operations for creating, retrieving, updating,
 * and deleting client information, as well as managing associated accounts.
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountService accountService;

    /**
     * Constructs the ClientService with dependencies.
     *
     * @param clientRepository the repository for client persistence
     * @param accountService the service for managing accounts
     */
    public ClientService(ClientRepository clientRepository, AccountService accountService) {
        this.clientRepository = clientRepository;
        this.accountService = accountService;
    }

    /**
     * Retrieves a client by its ID.
     *
     * @param id the client ID
     * @return the client as a DTO
     * @throws EntityNotFoundException if the client is not found
     */
    public ClientDTO getById(long id) {
        Client client = clientRepository.findById(id).orElse(null);

        if (client == null) {
            throw new EntityNotFoundException("Client with id " + id + " not found");
        }

        return MappingUtils.mapToClientDto(client);
    }

    /**
     * Adds a new client and automatically creates associated accounts for the client.
     *
     * - A "main" RUB account is created.
     * - A "secondary" USD account is created.
     *
     * @param clientDTO the client details
     * @return the newly added client as a DTO
     */
    @Transactional
    public ClientDTO addClient(ClientDTO clientDTO) {
        // Map DTO to entity and save the client
        Client client = MappingUtils.mapToClient(clientDTO);
        Client newClient = clientRepository.save(client);

        // Create default accounts for the client
        List<Account> accounts = new ArrayList<>();
        accounts.add(accountService.createAccount("RUB", 0d, "RUB", "main", newClient));
        accounts.add(accountService.createAccount("USD", 0d, "USD", "secondary", newClient));

        // Associate accounts with the client
        newClient.setAccounts(accounts);

        return MappingUtils.mapToClientDto(newClient);
    }

    /**
     * Updates an existing client's information.
     *
     * @param clientDTO the updated client details
     * @return the updated client as a DTO
     */
    public ClientDTO updateClient(ClientDTO clientDTO) {

        Client client = MappingUtils.mapToClient(clientDTO);
        clientRepository.save(client);

        Client updatedClient = clientRepository.findById(client.getId()).orElse(null);
        return MappingUtils.mapToClientDto(updatedClient);
    }

    /**
     * Deletes a client by its ID.
     *
     * @param id the client ID
     */
    public void deleteClient(long id) {
        clientRepository.deleteById(id);
    }
}

