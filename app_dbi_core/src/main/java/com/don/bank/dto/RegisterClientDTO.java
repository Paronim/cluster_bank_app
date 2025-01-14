package com.don.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterClientDTO {

    @Schema(description = "First name of the client", example = "Ivan")
    @NotNull(message = "message.validation.client.firstName")
    @Pattern(regexp = "^[A-Z].*", message = "message.validation.client.firstName.pattern")
    private String firstName;

    @Schema(description = "Last name of the client", example = "Ivanov")
    @NotNull(message = "message.validation.client.lastName")
    @Pattern(regexp = "^[A-Z].*", message = "message.validation.client.lastName.pattern")
    private String lastName;

    @Schema(description = "Phone of the client", example = "79001001010")
    @NotNull(message = "message.validation.client.phone")
    @Pattern(regexp = "^7\\d{10}$", message = "message.validation.client.phone.pattern")
    private String phone;

    @Schema(description = "Password of the client", example = "Password123!")
    @NotNull(message = "message.validation.login.password")
    @Pattern(regexp = "^.{8,}$", message = "message.validation.register.password.pattern_1")
    @Pattern(regexp = "^(?=.*[A-Z]).*$", message = "message.validation.register.password.pattern_2")
    @Pattern(regexp = "^(?=.*[0-9]).*$", message = "message.validation.register.password.pattern_3")
    @Pattern(regexp = "^(?=.*[@$!%*?&.\\-_]).*$", message = "message.validation.register.password.pattern_4")
    @Pattern(regexp = "^[A-Za-z\\d@$!%*?&.\\-_]+$", message = "message.validation.register.password.pattern_5")
    private String password;
}
