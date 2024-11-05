package com.donbank.config;

import com.donbank.config.config.ConfigObserver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Singleton class responsible for loading and managing application configuration properties.
 * This class reads properties from the `application.properties` file and provides
 * access to these properties throughout the application lifecycle.
 */
public class Config {

    private static volatile Config instance; // Singleton instance
    private final Properties properties = new Properties(); // Properties container
    private final List<ConfigObserver> observers = new ArrayList<>(); // List of observers

    /**
     * Private constructor that initializes the configuration by loading properties
     * from the configuration file.
     */
    private Config() {
        loadProperties();
    }

    /**
     * Loads properties from the `application.properties` file located in the classpath.
     * If the file is not found, a warning message is printed to the console.
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Unable to find application.properties");
                return;
            }
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace(); // Log any exception that occurs while loading properties
        }
    }

    /**
     * Returns the singleton instance of the Config class.
     * If the instance does not exist, it creates one in a thread-safe manner.
     *
     * @return the single instance of the Config class.
     */
    public static Config getInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    /**
     * Retrieves the property value associated with the specified key.
     *
     * @param key the key of the property to retrieve.
     * @return the value of the property, or null if the key does not exist.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Sets the property value associated with the specified key.
     * This method notifies all registered observers about the change.
     *
     * @param key   the key of the property to set.
     * @param value the new value for the property.
     */
    public void setProperty(String key, String value){
        properties.setProperty(key, value);
        notifyObservers(key, value); // Notify observers of the change
    }

    /**
     * Adds an observer that will be notified when properties are changed.
     *
     * @param configObserver the observer to add.
     */
    public void addObserver(ConfigObserver configObserver){
        observers.add(configObserver);
    }

    /**
     * Removes an observer so that it no longer receives notifications of property changes.
     *
     * @param observer the observer to remove.
     */
    public void removeObserver(ConfigObserver observer){
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers of a property change.
     *
     * @param key   the key of the changed property.
     * @param value the new value of the property.
     */
    private void notifyObservers(String key, String value){
        for(ConfigObserver configObserver: observers){
            configObserver.onConfigChanged(key, value); // Notify each observer of the change
        }
    }
}

