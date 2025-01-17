package com.don.bank.exception.transaction;

public class MoneyTransactionException extends RuntimeException{

    public MoneyTransactionException() {
        super("Error money transaction.");
    }

    public MoneyTransactionException(String message) {
        super(message);
    }
}
