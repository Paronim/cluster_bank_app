package com.common.models;

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
    @NotNull(message = "message.validation.client.firstName")
    @Pattern(regexp = "^\\p{Lu}.*", message = "message.validation.client.firstName.pattern")
    private String firstName;

    @Schema(description = "Last name of the client", example = "Ivanov")
    @NotNull(message = "message.validation.client.lastName")
    @Pattern(regexp = "^\\p{Lu}.*", message = "message.validation.client.lastName.pattern")
    private String lastName;

    @Schema(description = "Phone of the client", example = "79001001010")
    @NotNull(message = "message.validation.client.phone")
    @Pattern(regexp = "^7\\d{10}$", message = "message.validation.client.phone.pattern")
    private String phone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> accounts;
}
