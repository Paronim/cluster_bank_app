package com.donbank.repository;

import com.donbank.entity.Account;
import com.donbank.entity.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientRepositoryTest {
    private static final String TEST_FILE_PATH = "src/test/resources/clients.csv";
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() throws IOException {

        try (FileWriter writer = new FileWriter(TEST_FILE_PATH)) {
            writer.write("1,John,Doe\n");
            writer.write("2,Jane,Smith\n");
        }

        Account account1 = new Account(1, "RUB", 100.0, 1);
        Account account2 = new Account(2, "USD", 150.0, 2);
        List<Account> accounts = Arrays.asList(account1, account2);

        clientRepository = new ClientRepository(accounts, TEST_FILE_PATH);
    }

    @Test
    void testFindById_ExistingClient() {
        Client client = clientRepository.findById(1);

        assertNotNull(client);
        assertEquals(1, client.getId());
        assertEquals("John", client.getFirstName());
        assertEquals("Doe", client.getLastName());
        assertEquals(1, client.getAccounts().size());
        assertEquals("RUB", client.getAccounts().getFirst().getCurrency());
    }

    @Test
    void testFindById_NonExistingClient() {
        Client client = clientRepository.findById(3);
        assertNull(client);
    }

    @AfterEach
    void end(){
        new File(TEST_FILE_PATH).delete();
    }
}