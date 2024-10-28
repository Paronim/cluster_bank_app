package com.donbank.exception;

public class InsufficientFundsException extends Exception{

    public InsufficientFundsException(){
        super("There are insufficient funds in the account");
    }
}
