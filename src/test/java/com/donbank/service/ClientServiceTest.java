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

/**
 * Test class for verifying the functionality of {@link ClientService}.
 *
 * <p>
 * This class uses Mockito to create mocks and check the behavior of
 * {@link ClientService} in various scenarios. The tests include checking
 * the retrieval of a client by ID, both for existing and non-existing clients.
 * </p>
 */
class ClientServiceTest {

    private ClientService clientService;
    private ClientRepository clientRepository;

    /**
     * Method executed before each test, which sets up the necessary mocks
     * and service instances.
     */
    @BeforeEach
    public void setUp() {
        clientRepository = mock(ClientRepository.class);
        clientService = new ClientService(clientRepository);
    }

    /**
     * Checks that the method {@link ClientService#getClientById(int)} returns
     * a client if it exists.
     *
     * <p>
     * This test creates a mock client and sets up the repository to return
     * it. After calling the method, it checks that the returned client
     * is not null and its data matches the expected values.
     * </p>
     */
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

    /**
     * Checks that the method {@link ClientService#getClientById(int)} returns
     * null if a client with the given ID does not exist.
     *
     * <p>
     * This test sets up the repository to return null when querying for a
     * non-existing client and checks that the result of the method call
     * is also null.
     * </p>
     */
    @Test
    void testGetClientById_ClientDoesNotExist() {
        int clientId = 999;

        when(clientRepository.findById(clientId)).thenReturn(null);

        Client result = clientService.getClientById(clientId);

        assertNull(result);
    }
}