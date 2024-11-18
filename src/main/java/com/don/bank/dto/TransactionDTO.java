package com.don.bank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class TransactionDTO {

    private long id;

    @NotNull(message = "Amount is required")
    private double amount;

    private String transactionType;

    private Timestamp createdAt;

    @NotNull(message = "Account id is required")
    private long accountId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = "Recipient id is required")
    private Long recipientId;
}
