package com.don.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginClientDTO {

    @Schema(description = "Phone of the client", example = "79001001010")
    @NotNull(message = "Phone is required")
    @Pattern(regexp = "^7\\d{10}$", message = "Client number phone not valid")
    private String phone;

    @Schema(description = "Password of the client", example = "Password123!")
    @NotNull(message = "Password is required")
    private String password;
}
