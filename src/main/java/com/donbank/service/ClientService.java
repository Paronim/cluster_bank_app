package com.donbank.service;

import com.donbank.entity.Account;
import com.donbank.entity.Client;
import com.donbank.repository.ClientRepository;

import java.io.FileNotFoundException;
import java.util.List;

public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(List<Account> accounts) {
        this.clientRepository = new ClientRepository(accounts);
    }

    public Client getClientById(int id) {
        return clientRepository.findById(id);
    }

}
