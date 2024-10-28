package com.donbank.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a client with personal details and associated bank accounts.
 */
public class Client {

    /**
     * the unique identifier for the client.
     */
    private int id;
    /**
     *the first name of the client.
     */
    private String firstName;
    /**
     *the last name of the client.
     */
    private String lastName;
    /**
     *the list of accounts associated with the client.
     */
    private List<Account> accounts;

    /**
     * Default constructor for creating an empty Client object.
     */
    public Client() {
    }

    /**
     * Constructs a new Client with specified attributes.
     *
     * @param id        the unique identifier for the client.
     * @param firstName the first name of the client.
     * @param lastName  the last name of the client.
     * @param accounts  the list of accounts associated with the client.
     */
    public Client(int id, String firstName, String lastName, List<Account> accounts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = new ArrayList<>(accounts);
    }

    /**
     * Returns the unique identifier of the client.
     *
     * @return the client ID as an int.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the client.
     *
     * @param id the client ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the first name of the client.
     *
     * @return the first name as a String.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the client.
     *
     * @param firstName the first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the client.
     *
     * @return the last name as a String.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the client.
     *
     * @param lastName the last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the list of accounts associated with the client.
     *
     * @return a List of Account objects.
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Sets the list of accounts associated with the client.
     *
     * @param accounts the list of accounts to set.
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * Returns a string representation of the Client object in JSON-like format.
     *
     * @return a string representing the Client's state.
     */
    @Override
    public String toString() {
        return "{" +
                "\n\"id\": " + id + "," +
                "\n\"first_name\": \"" + firstName + "\"," +
                "\n\"last_name\": \"" + lastName + "\"," +
                "\n\"accounts\": " + accounts.toString() +
                "\n}";
    }
}
