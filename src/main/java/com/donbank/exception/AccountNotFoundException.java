package com.donbank.exception;

public class AccountNotFoundException extends Exception{
    public AccountNotFoundException(){
        super("An account with this currency not found");
    }

}
