package com.donbank.exception;

/**
 * Exception thrown when an operation cannot be completed due to insufficient funds in an account.
 */
public class InsufficientFundsException extends Exception {

    /**
     * Constructs a new InsufficientFundsException with a default error message.
     */
    public InsufficientFundsException() {
        super("There are insufficient funds in the account");
    }
}