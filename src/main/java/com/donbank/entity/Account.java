package com.donbank.entity;

import java.io.Serializable;

public class Account implements Serializable {

    private int id;
    private String currency;
    private double balance;
    private int clientId;

    public Account() {}

    public Account(int id, String currency, double balance, int clientId) {
        this.id = id;
        this.currency = currency;
        this.balance = balance;
        this.clientId = clientId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString(){
        return "{\n" +
                "\"id\": " + id + "," +
                "\n\"currency\": \"" + currency + "\"," +
                "\n\"balance\": " + balance +
                "\n}";
    }
}
