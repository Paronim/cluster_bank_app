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

/**
 * Test class for verifying the functionality of {@link ClientRepository}.
 *
 * <p>
 * This class includes tests for finding clients by their IDs, both for existing
 * and non-existing clients, using a temporary CSV file for testing purposes.
 * </p>
 */
class ClientRepositoryTest {
    private static final String TEST_FILE_PATH = "src/test/resources/clients.csv";
    private ClientRepository clientRepository;

    /**
     * Method executed before each test, which sets up the necessary test data
     * in a temporary CSV file and initializes the client repository.
     */
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

    /**
     * Tests finding an existing client by ID.
     *
     * <p>
     * This test verifies that when a valid client ID is provided, the corresponding
     * client object is returned with the correct attributes.
     * </p>
     */
    @Test
    void testFindById_ExistingClient() {
        Client client = clientRepository.findById(1);

        assertNotNull(client);
        assertEquals(1, client.getId());
        assertEquals("John", client.getFirstName());
        assertEquals("Doe", client.getLastName());
        assertEquals(1, client.getAccounts().size());
        assertEquals("RUB", client.getAccounts().get(0).getCurrency());
    }

    /**
     * Tests finding a non-existing client by ID.
     *
     * <p>
     * This test ensures that when an invalid client ID is provided, the method
     * returns null, indicating that no client was found.
     * </p>
     */
    @Test
    void testFindById_NonExistingClient() {
        Client client = clientRepository.findById(3);
        assertNull(client);
    }

    /**
     * Method executed after each test, which cleans up the temporary test file
     * created for the test scenarios.
     */
    @AfterEach
    void end(){
        new File(TEST_FILE_PATH).delete();
    }
}