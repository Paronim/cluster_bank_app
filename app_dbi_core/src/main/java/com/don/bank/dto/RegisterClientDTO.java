package com.don.bank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegisterClientDTO {

    @Schema(description = "First name of the client", example = "Ivan")
    @NotNull(message = "First name is required")
    @Pattern(regexp = "^[A-Z].*", message = "Client first name must start with an uppercase letter")
    private String firstName;

    @Schema(description = "Last name of the client", example = "Ivanov")
    @NotNull(message = "Last name is required")
    @Pattern(regexp = "^[A-Z].*", message = "Client first name must start with an uppercase letter")
    private String lastName;

    @Schema(description = "Phone of the client", example = "79001001010")
    @NotNull(message = "Phone is required")
    @Pattern(regexp = "^7\\d{10}$", message = "Client number phone not valid")
    private String phone;

    @Schema(description = "Password of the client", example = "Password123!")
    @NotNull(message = "Password is required")
//    @Pattern(regexp = "^.{8,}$", message = "The password must be at least 8 characters")
//    @Pattern(regexp = "(?=.*[A-Z])", message = "The password must have at least one uppercase letter")
//    @Pattern(regexp = "(?=.*[0-9])", message = "The password must have at least one number")
//    @Pattern(regexp = "(?=.*[@$!%*?&.\\-_])", message = "The password must have at least one special character (@$!%*?&.-_)")
//    @Pattern(regexp = "^[A-Za-z\\d@$!%*?&.\\-_]+$", message = "The password contains invalid characters")
    private String password;
}
