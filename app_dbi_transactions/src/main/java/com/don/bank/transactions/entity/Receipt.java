package com.don.bank.transactions.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.sql.Timestamp;

@Document(indexName = "receipt")
@Data
@Builder
public class Receipt {

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW,
        TRANSFER
    }

    @Id
    private long id;

    private double amount;
    private TransactionType transactionType;
    private Long createdAt;
    private long accountId;
    private Long recipientId;
}
