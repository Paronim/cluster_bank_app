package com.don.bank.exception.client;


public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException() {
        super("Client not found.");
    }

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientNotFoundException(Throwable cause) {
        super(cause);
    }
}

