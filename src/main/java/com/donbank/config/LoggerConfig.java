package com.donbank.config;

import com.donbank.config.config.ConfigObserver;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.*;

/**
 * Singleton class responsible for configuring and managing the application's logger.
 * It sets up logging output based on configuration properties and provides access to the logger instance.
 * This class implements the ConfigObserver interface to react to changes in configuration.
 */
public class LoggerConfig implements ConfigObserver {

    private static volatile LoggerConfig instance;
    private static final Logger logger = Logger.getLogger(LoggerConfig.class.getName());
    private final Config config = Config.getInstance();

    /**
     * Private constructor that initializes the logger configuration.
     * It sets up the logger based on the application's configuration properties
     * and registers this instance as an observer of configuration changes.
     */
    private LoggerConfig() {
        configureLogger();
        config.addObserver(this);
    }

    /**
     * Configures the logger based on properties defined in the application configuration.
     * It removes default handlers and adds new ones based on the logging output type specified.
     * Supports logging to the console or to specific log files based on log levels (ERROR, WARNING, INFO).
     */
    private void configureLogger() {
        final String logOutput = config.getProperty("logging.param");

        Logger rootLogger = Logger.getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        logger.setLevel(Level.FINE);

        if (Objects.equals(logOutput, "console")) {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);
        } else {
            try {
                FileHandler fileHandlerError = new FileHandler(config.getProperty("logging.path.error"), true);
                fileHandlerError.setFormatter(new SimpleFormatter());
                fileHandlerError.setFilter(record -> record.getLevel().intValue() >= Level.SEVERE.intValue());
                logger.addHandler(fileHandlerError);

                FileHandler fileHandlerWarning = new FileHandler(config.getProperty("logging.path.warning"), true);
                fileHandlerWarning.setFormatter(new SimpleFormatter());
                fileHandlerWarning.setFilter(record -> record.getLevel().intValue() == Level.WARNING.intValue());
                logger.addHandler(fileHandlerWarning);

                FileHandler fileHandlerInfo = new FileHandler(config.getProperty("logging.path.info"), true);
                fileHandlerInfo.setFormatter(new SimpleFormatter());
                fileHandlerInfo.setFilter(record -> record.getLevel().intValue() <= Level.INFO.intValue());
                logger.addHandler(fileHandlerInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the singleton instance of LoggerConfig.
     * If the instance does not exist, it creates one in a thread-safe manner.
     *
     * @return the single instance of LoggerConfig.
     */
    public static LoggerConfig getInstance() {
        if (instance == null) {
            synchronized (LoggerConfig.class) {
                if (instance == null) {
                    instance = new LoggerConfig();
                }
            }
        }
        return instance;
    }

    /**
     * Returns the configured logger instance.
     *
     * @return the logger used for logging events.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Reacts to configuration changes.
     * If the changed key starts with "logging.", it reconfigures the logger.
     *
     * @param key      the key of the changed property.
     * @param newValue the new value of the property.
     */
    @Override
    public void onConfigChanged(String key, String newValue) {
        if (key.startsWith("logging.")) {
            configureLogger();
        }
    }
}
