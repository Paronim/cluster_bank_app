package com.don.bank.controller;

import com.don.bank.dto.LoginClientDTO;
import com.don.bank.dto.RegisterClientDTO;
import com.don.bank.service.AuthService;
import com.don.bank.util.JWT.JWTTokenCookie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
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

    @Operation(summary = "Authorization client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "JSON with id client"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated LoginClientDTO loginClientDTO, HttpServletResponse response) {
        try {
            Map<String, Object> result = authService.login(loginClientDTO);

            JWTTokenCookie.addToken(response, result.get("token").toString());

            return ResponseEntity.ok(Map.of("id", result.get("id"), "redirect", "/"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Invalid password"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error in login"));
        }
    }

    @Operation(summary = "Add a new client", description = "Creates a new client with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created client"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity register(
            @Parameter(description = "Client data to be created", required = true) @RequestBody @Validated RegisterClientDTO registerClientDTO, HttpServletResponse response) {
        try {
            authService.register(registerClientDTO);

            return new ResponseEntity<>(Map.of("message", "Register successfully", "redirect", "/login"), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message","Error while register client"));
        }
    }

}
