package com.don.bank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Data
@Builder
public class ClientDTO {

    private long id;

    @NotNull(message = "First name is required")
    @Pattern(regexp = "^[A-Z].*", message = "Client first name must start with an uppercase letter")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Pattern(regexp = "^[A-Z].*", message = "Client first name must start with an uppercase letter")
    private String lastName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> accounts;
}
