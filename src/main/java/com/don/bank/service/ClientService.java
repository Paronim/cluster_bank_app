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

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountService accountService;

    public ClientService(ClientRepository clientRepository, AccountService accountService) {
        this.clientRepository = clientRepository;
        this.accountService = accountService;
    }

    public ClientDTO getById(long id) {
        Client client = clientRepository.findById(id).orElse(null);

        if(client == null) {
            throw new EntityNotFoundException("Client with id " + id + " not found");
        }

        return MappingUtils.mapToClientDto(client);
    }

    @Transactional
    public ClientDTO addClient(ClientDTO clientDTO) {
        Client client = MappingUtils.mapToClient(clientDTO);
        Client newClient = clientRepository.save(client);

        List<Account> accounts = new ArrayList<>();

        accounts.add(accountService.createAccount("RUB", 0d, "RUB", "main", newClient));
        accounts.add(accountService.createAccount("USD", 0d, "USD", "secondary", newClient));

        newClient.setAccounts(accounts);

        return MappingUtils.mapToClientDto(newClient);
    }

    public ClientDTO updateClient(ClientDTO clientDTO) {
        Client client = MappingUtils.mapToClient(clientDTO);
        clientRepository.save(client);
        Client updatedClient = clientRepository.findById(client.getId()).orElse(null);
        return MappingUtils.mapToClientDto(updatedClient);
    }

    public void deleteClient(long id) {
        clientRepository.deleteById(id);
    }
}
