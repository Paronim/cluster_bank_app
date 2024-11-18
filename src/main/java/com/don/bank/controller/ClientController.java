package com.don.bank.controller;

import com.don.bank.dto.ClientDTO;
import com.don.bank.entity.Client;
import com.don.bank.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;

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
        } catch (EntityNotFoundException e) {
            log.warn("Client not found: {}", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Error while getting client by id", HttpStatus.INTERNAL_SERVER_ERROR);
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
        } catch (EntityNotFoundException e) {
            log.warn("Client not found: {}", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Error while getting client accounts", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Add a new client", description = "Creates a new client with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created client"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity addClient(
            @Parameter(description = "Client data to be created", required = true) @RequestBody @Validated ClientDTO clientDTO) {
        try {
            ClientDTO newClient = clientService.addClient(clientDTO);
            return new ResponseEntity<>(newClient, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Error while adding client", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update a client", description = "Updates an existing client's details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated client"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public ResponseEntity updateClient(
            @Parameter(description = "Updated client data", required = true) @RequestBody @Validated ClientDTO clientDTO) {
        try {
            clientService.getById(clientDTO.getId());
            ClientDTO newClient = clientService.updateClient(clientDTO);
            return ResponseEntity.ok(newClient);
        } catch (EntityNotFoundException e) {
            log.warn("Client not found: {}", clientDTO.getId());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Error while updating client", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a client", description = "Deletes a client by their unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted client"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(
            @Parameter(description = "Unique identifier of the client to delete", required = true) @PathVariable Long id) {
        try {
            clientService.getById(id);
            clientService.deleteClient(id);
            return ResponseEntity.ok("Deleted client successfully: " + id);
        } catch (EntityNotFoundException e) {
            log.warn("Client not found: {}", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Error while deleting client", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
