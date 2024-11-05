package com.donbank.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    private Config config;

    @BeforeEach
    void setUp(){
        config = Config.getInstance();
        config.setProperty("logging.param", "console");
    }

    @Test
    void testSingletonInstance() {
        Config anotherInstance = Config.getInstance();

        assertSame(config, anotherInstance, "DatabaseConnector should be a singleton");
    }

    @Test
    void testGetPropertyIsNotNull() {
        Config config = Config.getInstance();

        assertNotNull(config.getProperty("db.url"), "Property value should not be null");
    }

    @Test
    void  testSetProperty() {
        Config config = Config.getInstance();

        String testValue = "test";
        config.setProperty("test", testValue);

        assertEquals(config.getProperty("test"), testValue);
    }
}