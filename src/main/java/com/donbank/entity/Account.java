package com.donbank.entity;

import java.io.Serializable;

/**
 * Represents a bank account with a unique ID, currency type, balance, and associated client ID.
 * Implements Serializable to allow for object serialization.
 */
public class Account implements Serializable {

    /**
     * the unique identifier for the account.
     */
    private int id;
    /**
     * the currency type of the account (e.g., RUB, USD).
     */
    private String currency;
    /**
     *the current balance of the account.
     */
    private double balance;
    /**
     *the unique identifier of the client associated with this account.
     */
    private int clientId;

    /**
     * Default constructor for creating an empty Account object.
     */
    public Account() {}

    /**
     * Constructs a new Account with specified attributes.
     *
     * @param id        the unique identifier for the account.
     * @param currency  the currency type of the account (e.g., RUB, USD).
     * @param balance   the current balance of the account.
     * @param clientId  the unique identifier of the client associated with this account.
     */
    public Account(int id, String currency, double balance, int clientId) {
        this.id = id;
        this.currency = currency;
        this.balance = balance;
        this.clientId = clientId;
    }

    /**
     * Returns the currency of the account.
     *
     * @return the currency type as a String.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the account.
     *
     * @param currency the currency type to set.
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Returns the current balance of the account.
     *
     * @return the balance as a double.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the account.
     *
     * @param balance the balance to set.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Returns the client ID associated with this account.
     *
     * @return the client ID as an int.
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the client ID associated with this account.
     *
     * @param clientId the client ID to set.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Returns a string representation of the Account object in JSON-like format.
     *
     * @return a string representing the Account's state.
     */
    @Override
    public String toString() {
        return "{\n" +
                "\"id\": " + id + "," +
                "\n\"currency\": \"" + currency + "\"," +
                "\n\"balance\": " + balance +
                "\n}";
    }
}
