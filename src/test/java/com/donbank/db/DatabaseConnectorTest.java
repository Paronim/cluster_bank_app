package com.donbank.db;

import com.donbank.config.Config;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectorTest {

    private DatabaseConnector databaseConnector;

    @BeforeEach
    void setUp(){
        Config.getInstance().setProperty("logging.param", "console");
        databaseConnector = DatabaseConnector.getInstance();
    }

    @AfterEach
    void end(){
        databaseConnector.closeConnection();
    }

    @Test
    void testSingletonInstance() {
        DatabaseConnector anotherInstance = DatabaseConnector.getInstance();

        assertSame(databaseConnector, anotherInstance, "DatabaseConnector should be a singleton");
    }

    @Test
    void testConnectionIsNotNull() {
        Connection connection = databaseConnector.getConnection();

        assertNotNull(connection, "Database connection should not be null");
    }

    @Test
    void testConnectionIsValid() throws SQLException {
        Connection connection = databaseConnector.getConnection();

        boolean testConnection = connection.isValid(5);

        assertTrue(testConnection, "Database connection should be valid");
    }

    @Test
    void testCloseConnection() throws SQLException {
        Connection connection = databaseConnector.getConnection();
        databaseConnector.closeConnection();
        assertTrue(connection.isClosed(), "Database connection should be closed after calling closeConnection");
    }
}