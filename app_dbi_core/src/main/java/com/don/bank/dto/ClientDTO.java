package com.don.bank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ClientDTO {

    @Schema(description = "Unique identifier of the client", example = "10000")
    private long id;

    @Schema(description = "First name of the client", example = "Ivan")
    @NotNull(message = "First name is required")
    @Pattern(regexp = "^\\p{Lu}.*", message = "Client first name must start with an uppercase letter")
    private String firstName;

    @Schema(description = "Last name of the client", example = "Ivanov")
    @NotNull(message = "Last name is required")
    @Pattern(regexp = "^\\p{Lu}.*", message = "Client last name must start with an uppercase letter")
    private String lastName;

    @Schema(description = "Phone of the client", example = "79001001010")
    @NotNull(message = "Phone is required")
    @Pattern(regexp = "^7\\d{10}$", message = "Client number phone not valid")
    private String phone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> accounts;
}
