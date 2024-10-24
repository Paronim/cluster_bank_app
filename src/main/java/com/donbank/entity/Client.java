package com.donbank.entity;

public class Client {

    private int id;
    private String firstName;
    private String lastName;
    private Account[] account;

    public Client() {
    }

    public Client(int id, String firstName, String lastName, Account[] account) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.account = account;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Account[] getAccount() {
        return account;
    }

    public void setAccount(Account[] account) {
        this.account = account;
    }
}
