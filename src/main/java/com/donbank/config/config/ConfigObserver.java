package com.donbank.config.config;

/**
 * Interface representing an observer that gets notified of changes
 * in configuration properties.
 *
 * Implementing classes should define specific behavior that occurs
 * when a configuration property changes.
 */
public interface ConfigObserver {

    /**
     * Called when a configuration property has changed.
     *
     * @param key     the key of the property that was changed.
     * @param newValue the new value of the property.
     */
    void onConfigChanged(String key, String newValue);
}
