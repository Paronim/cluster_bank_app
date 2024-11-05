package com.donbank.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * The Account class represents a bank account.
 * It contains information about the account's unique identifier, currency type,
 * current balance, and the associated client ID.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account implements Serializable {

    /**
     * Enum representing the available currencies for accounts.
     */
    public enum Currency {
        /**
         * Russian Ruble
         */
        RUB,
        /**
         * US Dollar
         */
        USD
    }

    private Account(Builder builder) {
        this.id = builder.id;
        this.currency = builder.currency;
        this.balance = builder.balance;
        this.clientId = builder.clientId;
    }

    /**
     * The unique identifier for the account.
     */
    private long id;

    /**
     * The currency type of the account (e.g., RUB, USD).
     */
    private Currency currency;

    /**
     * The current balance of the account.
     */
    private double balance;

    /**
     * The unique identifier of the client associated with this account.
     */
    private long clientId;

    /**
     * Returns a string representation of the Account object in JSON-like format.
     *
     * @return a string representing the Account's state.
     */
    @Override
    public String toString() {
        return "{\n" +
                "\"id\": " + id + "," +
                "\n\"currency\": \"" + currency + "\"," +
                "\n\"balance\": " + balance +
                "\n}";
    }

    /**
     * Builder class for constructing Account instances.
     */
    public static class Builder {
        private long id;
        private Currency currency;
        private double balance;
        private long clientId;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder setBalance(double balance) {
            this.balance = balance;
            return this;
        }

        public Builder setClientId(long clientId) {
            this.clientId = clientId;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }
}
