package com.don.bank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class TransactionDTO {

    @Schema(description = "Unique identifier of the transaction", example = "Ivan")
    private long id;

    @Schema(description = "Amount of the transaction", example = "100")
    @NotNull(message = "Amount is required")
    private double amount;

    @Schema(description = "Transaction type of the transaction (DEPOSIT | WITHDRAW | TRANSFER)", example = "100")
    private String transactionType;

    @Schema(description = "Date and time of the transaction", example = "2024-01-01 00:00:00.0")
    private Timestamp createdAt;

    @Schema(description = "Account id of the transaction", example = "10000")
    @NotNull(message = "Account id is required")
    private long accountId;

    @Schema(description = "Account recipient id of the transaction", example = "10000")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = "Recipient id is required")
    private Long recipientId;
}
