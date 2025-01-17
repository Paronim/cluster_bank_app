package com.common.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginClientDTO {

    @Schema(description = "Phone of the client", example = "79001001010")
    @NotNull(message = "message.validation.client.phone")
    @Pattern(regexp = "^7\\d{10}$", message = "message.validation.client.phone.pattern")
    private String phone;

    @Schema(description = "Password of the client", example = "Password123!")
    @NotNull(message = "message.validation.login.password")
    private String password;
}
