package com.don.bank.controller;

import com.don.bank.dto.ClientDTO;
import com.don.bank.exception.client.ClientNotFoundException;
import com.don.bank.service.ClientService;
import com.don.bank.util.JWT.JWTTokenCookie;
import com.don.bank.util.JWT.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    private ClientService clientService;

    private JwtUtils jwtUtils;

    private final MessageSource messageSource;

    public ClientController(ClientService clientService, JwtUtils jwtUtils, MessageSource messageSource) {
        this.clientService = clientService;
        this.jwtUtils = jwtUtils;
        this.messageSource = messageSource;
    }

    @Operation(summary = "Retrieve a client by ID", description = "Fetches a client by their unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved client"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity getClientById(
            @Parameter(description = "Unique identifier of the client", required = true) @PathVariable long id) {
        try {
            ClientDTO client = clientService.getById(id);
            return ResponseEntity.ok(client);
        } catch (ClientNotFoundException e) {
            log.warn("Client not found: {}", id);
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message", "Error while getting client by id"));
        }
    }

    @Operation(summary = "Retrieve accounts of a client", description = "Fetches all accounts associated with a specific client.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved client accounts"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/accounts")
    public ResponseEntity getClientAccounts(
            @Parameter(description = "Unique identifier of the client", required = true) @PathVariable long id) {
        try {
            ClientDTO client = clientService.getById(id);
            return ResponseEntity.ok(client.getAccounts());
        } catch (ClientNotFoundException e) {
            log.warn("Client not found: {}", id);
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message","Error while getting client accounts"));
        }
    }

    @Operation(summary = "Update a client", description = "Updates an existing client's details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated client"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping
    public ResponseEntity updateClient(
            @Parameter(description = "Updated client data", required = true) @RequestBody @Validated ClientDTO clientDTO, Locale locale) {
        try {
            clientService.getById(clientDTO.getId());
            clientService.updateClient(clientDTO);
            return ResponseEntity.ok(Map.of("message", messageSource.getMessage("message.client.update.ok", null, locale)));
        } catch (ClientNotFoundException e) {
            log.warn("Client not found: {}", clientDTO.getId());
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message",messageSource.getMessage("message.client.error.update", null, locale)));
        }
    }

    @Operation(summary = "Delete a client", description = "Deletes a client by their unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted client"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(
            @Parameter(description = "Unique identifier of the client to delete", required = true) @PathVariable Long id, HttpServletResponse response) {
        try {
            clientService.getById(id);
            clientService.deleteClient(id);
            JWTTokenCookie.removeToken(response);
            return ResponseEntity.ok(Map.of("message","Deleted client successfully: " + id, "redirect", "/login"));
        } catch (ClientNotFoundException e) {
            log.warn("Client not found: {}", id);
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message","Error while deleting client"));
        }
    }

    @GetMapping("/id")
    public ResponseEntity getIdByToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();

                    try {
                        return ResponseEntity.ok(Map.of("id", Long.parseLong(jwtUtils.extractUsername(token))));
                    } catch (Exception e) {
                        return new ResponseEntity<>(Map.of("message", "Error get client"), HttpStatus.UNAUTHORIZED);
                    }
                }
            }
        }

        return new ResponseEntity<>(Map.of("message", "Token is missing"), HttpStatus.UNAUTHORIZED);
    }
}
