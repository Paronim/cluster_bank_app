package com.don.common.models;

import io.swagger.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
public class AccountDTO {

    @Schema(description = "Unique identifier of the account", example = "10000")
    private long id;

    @Schema(description = "Currency of the account (USD | RUB)", example = "USD")
    @NotNull(message = "message.validation.account.currency")
    private String currency;

    @Schema(description = "Current balance in the account", example = "1500.00")
    private double balance;

    @Schema(description = "Name of the account", example = "Savings Account")
    @NotNull(message = "message.validation.account.name")
    private String name;

    @Schema(description = "Type of the account, e.g.")
    private String type;

    @Schema(description = "Client ID to which the account belongs", example = "10100")
    private long clientId;
}
