package com.donbank.service;

import com.donbank.config.Config;
import com.donbank.entity.Account;
import com.donbank.entity.Client;
import com.donbank.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ClientServiceTest {

    private ClientService clientService;
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        clientRepository = mock(ClientRepository.class);
        clientService = new ClientService(clientRepository);
    }

    @Test
    void testImportCSVClients(){
        assertDoesNotThrow(() ->
            clientService.importCSVClients(new ArrayList<>(), Config.getInstance().getProperty("csv.clients")));

    }
}