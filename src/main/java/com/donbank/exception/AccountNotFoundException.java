package com.donbank.exception;

/**
 * Exception thrown when an account with a specified currency is not found.
 */
public class AccountNotFoundException extends Exception {

    /**
     * Constructs a new AccountNotFoundException with a default error message.
     */
    public AccountNotFoundException() {
        super("An account with this currency not found");
    }
}
