package com.donbank.db;

import com.donbank.config.Config;
import com.donbank.config.LoggerConfig;
import com.donbank.config.config.ConfigObserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton class responsible for managing database connections.
 * It ensures that only one instance of the database connection is created
 * and provides methods to retrieve and close the connection.
 * This class implements the ConfigObserver interface to react to changes in configuration related to the database.
 */
public class DatabaseConnector implements ConfigObserver {

    private final Logger logger = LoggerConfig.getInstance().getLogger();
    private static volatile DatabaseConnector instance;
    private Connection connection;

    /**
     * Private constructor to establish the database connection.
     * It retrieves connection details from the configuration and attempts to connect to the database.
     * Registers this instance as an observer for configuration changes.
     */
    private DatabaseConnector() {
        Config.getInstance().addObserver(this);
        initializeConnector();
    }

    /**
     * Initializes the database connection using properties from the configuration.
     * Logs a success message upon successful connection or logs an error message if the connection fails.
     */
    private void initializeConnector() {
        try {
            Config config = Config.getInstance();
            connection = DriverManager.getConnection(
                    config.getProperty("db.url"),
                    config.getProperty("db.user"),
                    config.getProperty("db.password")
            );
            logger.log(Level.INFO, "Connected to the database successfully.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Returns the singleton instance of the DatabaseConnector.
     * If the instance does not exist, it creates one in a thread-safe manner.
     *
     * @return the single instance of DatabaseConnector.
     */
    public static DatabaseConnector getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnector.class) {
                if (instance == null) {
                    instance = new DatabaseConnector();
                }
            }
        }
        return instance;
    }

    /**
     * Retrieves the active database connection.
     * If the connection is closed, it reinitializes the connector to establish a new connection.
     *
     * @return the current database connection.
     */
    public Connection getConnection() {
        try {
            if (connection.isClosed()) {
                initializeConnector();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return connection;
    }

    /**
     * Closes the database connection if it is open.
     * Logs the status of the connection closure.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.log(Level.INFO, "Database connection closed.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Reacts to configuration changes.
     * If the changed key starts with "db.", it reinitializes the database connection.
     *
     * @param key      the key of the changed property.
     * @param newValue the new value of the property.
     */
    @Override
    public void onConfigChanged(String key, String newValue) {
        if (key.startsWith("db.")) {
            initializeConnector();
        }
    }
}
