package com.donbank.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.*;

import static org.junit.jupiter.api.Assertions.*;

class LoggerConfigTest {

    private LoggerConfig loggerConfig;
    private Config config;

    @BeforeEach
    void setUp(){
        config = Config.getInstance();
        config.setProperty("logging.param", "console");
        loggerConfig = LoggerConfig.getInstance();
    }

    @AfterEach
    void end() throws IOException {
        Files.walk(Path.of("src/test/resources/"))
                .filter(Files::isRegularFile)
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        throw new RuntimeException("Failed to delete test log files", e);
                    }
                });
    }

    @Test
    void testSingletonInstance() {
        LoggerConfig anotherInstance = LoggerConfig.getInstance();

        assertSame(loggerConfig, anotherInstance, "DatabaseConnector should be a singleton");
    }

    @Test
    void testConsoleLogging() {
        config.setProperty("logging.param", "console");

        Logger logger = loggerConfig.getLogger();

        boolean hasConsoleHandler = false;
        for (Handler handler : logger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                hasConsoleHandler = true;
                break;
            }
        }

        assertTrue(hasConsoleHandler, "Logger should have a ConsoleHandler when 'logging.param' is set to 'console'.");
    }

    @Test
    void testFileLoggingSetup() throws IOException {
        config.setProperty("logging.param", "file");
        config.setProperty("logging.path.error", "src/test/resources/error.log");
        config.setProperty("logging.path.warning", "src/test/resources/warning.log");
        config.setProperty("logging.path.info","src/test/resources/info.log");

        Logger logger = loggerConfig.getLogger();

        boolean hasErrorFileHandler = false;
        boolean hasWarningFileHandler = false;
        boolean hasInfoFileHandler = false;

        for (Handler handler : logger.getHandlers()) {

            if (handler instanceof FileHandler) {

                logger.log(Level.INFO, "test");
                logger.log(Level.WARNING, "test");
                logger.log(Level.SEVERE, "test");

                File logError = new File(config.getProperty("logging.path.error"));
                File logWarning = new File(config.getProperty("logging.path.warning"));
                File logInfo = new File(config.getProperty("logging.path.info"));

                if (logError.exists() && logError.length() > 0) hasErrorFileHandler = true;
                if (logWarning.exists() && logWarning.length() > 0) hasWarningFileHandler = true;
                if (logWarning.exists() && logInfo.length() > 0) hasInfoFileHandler = true;

                handler.close();
            }
        }

        assertTrue(hasErrorFileHandler, "Logger should have an Error FileHandler when 'logging.path.error' is configured.");
        assertTrue(hasWarningFileHandler, "Logger should have a Warning FileHandler when 'logging.path.warning' is configured.");
        assertTrue(hasInfoFileHandler, "Logger should have an Info FileHandler when 'logging.path.info' is configured.");
    }
}