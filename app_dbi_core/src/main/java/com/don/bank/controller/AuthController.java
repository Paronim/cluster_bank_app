package com.don.bank.controller;

import com.don.bank.dto.LoginClientDTO;
import com.don.bank.dto.RegisterClientDTO;
import com.don.bank.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;

    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated LoginClientDTO loginClientDTO) {
        try {
            return ResponseEntity.ok(Map.of("message" , "Successfully auth", "token", authService.login(loginClientDTO)));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid phone number or password"));
        }
    }

    @Operation(summary = "Add a new client", description = "Creates a new client with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created client"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity register(
            @Parameter(description = "Client data to be created", required = true) @RequestBody @Validated RegisterClientDTO registerClientDTO) {
        try {
            authService.register(registerClientDTO);
            return new ResponseEntity<>(Map.of("message", "Register successfully"), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message","Error while register client"));
        }
    }
}
