package com.donbank.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * The Client class represents a bank client.
 * It includes the client's personal information and their bank accounts.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {

    /**
     * The unique identifier for the client.
     */
    private long id;

    /**
     * The first name of the client.
     */
    private String firstName;

    /**
     * The last name of the client.
     */
    private String lastName;

    /**
     * The list of accounts associated with the client.
     */
    private List<Account> accounts;

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
