package com.donbank.service;

import com.donbank.entity.Account;
import com.donbank.entity.Client;
import com.donbank.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientServiceTest {


    private ClientService clientService;
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        clientRepository = mock(ClientRepository.class);
        clientService = new ClientService(clientRepository);
    }

    @Test
    void testGetClientById_ClientExists() {
        int clientId = 1;
        List<Account> accountList = new ArrayList<>();
        Client mockClient = new Client(clientId, "John",  "Doe", accountList);

        when(clientRepository.findById(clientId)).thenReturn(mockClient);

        Client result = clientService.getClientById(clientId);

        assertNotNull(result);
        assertEquals(clientId, result.getId());
        assertEquals("John Doe", result.getFirstName() + " " + result.getLastName());
    }

    @Test
    void testGetClientById_ClientDoesNotExist() {
        int clientId = 999;

        when(clientRepository.findById(clientId)).thenReturn(null);

        Client result = clientService.getClientById(clientId);

        assertNull(result);
    }
}