/**
 * Represents a financial transaction in the banking system.
 * A transaction has an associated account, a transaction type, an amount,
 * and a timestamp indicating when it was created.
 */
package com.donbank.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The Transaction class encapsulates all relevant information about a financial transaction.
 * It provides constructors, getters, and setters for its fields.
 * The transaction type can either be a contribution or a withdrawal.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Transaction implements Serializable {

    /**
     * Enum representing the type of transaction.
     */
    public enum TransactionType {
        /**
         * Represents a contribution transaction
         */
        contribute,
        /**
         * Represents a withdrawal transaction.
         */
        withdraw
    }

    /**
     * The unique identifier for the transaction.
     */
    private long id;

    /**
     * The amount of money involved in the transaction.
     */
    private double amount;

    /**
     * The type of transaction (contribution or withdrawal).
     */
    private TransactionType transactionType;

    /**
     * The timestamp when the transaction was created.
     */
    private Timestamp createdAt;

    /**
     * The account associated with this transaction.
     */
    private Account account;

    private Transaction(Builder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
        this.transactionType = builder.transactionType;
        this.createdAt = builder.createdAt;
        this.account = builder.account;
    }
    /**
     * Returns a string representation of the Transaction object in JSON format.
     *
     * @return a string representing the transaction details.
     */
    @Override
    public String toString() {
        return "{" +
                "\n\"id\": " + id + "," +
                "\n\"amount\": \"" + amount + "\"," +
                "\n\"transaction_type\": \"" + transactionType + "\"," +
                "\n\"created_at\": " + createdAt + "," +
                "\n\"account\": " + account.toString() +
                "\n}";
    }

    /**
     * Builder class for constructing Transaction instances.
     */
    public static class Builder {
        private long id;
        private double amount;
        private TransactionType transactionType;
        private Timestamp createdAt;
        private Account account;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setTransactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setAccount(Account account) {
            this.account = account;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
