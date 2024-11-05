package com.donbank.repository;

import com.donbank.config.Config;
import com.donbank.db.DatabaseConnector;
import com.donbank.entity.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientRepositoryTest {
    private ClientRepository clientRepository;
    private Connection connection;
    private Config config;
    private long id = 10000L;

    @BeforeEach
    void setUp() throws SQLException {

        config = Config.getInstance();
        config.setProperty("db.url", "jdbc:postgresql://localhost:5432/test.app_dbi");
        config.setProperty("logging.param", "console");
        connection = DatabaseConnector.getInstance().getConnection();

        clientRepository = new ClientRepository(id, new ArrayList<>());

        connection.setAutoCommit(false);
    }

    @AfterEach
    void end() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    void testGetClient() {
        assertEquals(clientRepository.getClient().getId(), id);
    }


    @Test
    void testAddClient() throws SQLException {
        String name = "test";
        long id = clientRepository.addClient(name, name);
        Client client = clientRepository.getClient(id, new ArrayList<>());

        assertEquals(client.getFirstName(), name);
        assertEquals(client.getLastName(), name);
        assertEquals(client.getId(), id);

    }

    @Test
    void testGetListClientsFromCSV_FileNotFound() {
        String invalidFilePath = "src/test/resources/invalid_clients.csv";

        File tempFile = new File(invalidFilePath);

        assertFalse(tempFile.exists());

        assertThrows(FileNotFoundException.class, () -> clientRepository.getListClientsFromCSV(new ArrayList<>(),invalidFilePath));

    }

    @Test
    void testGetListClientsFromCSV_Success() throws IOException {
        String testFilePath = "src/test/resources/test_clients.csv";
        File testFile = new File(testFilePath);
        FileWriter writer = new FileWriter(testFile);

        writer.write("1,Test,Test\n");
        writer.write("2,Test,Test\n");
        writer.write("3,Test,Test\n");
        writer.close();

        List<Client> clients = clientRepository.getListClientsFromCSV(new ArrayList<>(), testFilePath);
        assertNotNull(clients);
        assertEquals(3, clients.size());

        testFile.delete();
    }

}