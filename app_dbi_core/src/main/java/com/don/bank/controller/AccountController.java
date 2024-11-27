package com.don.bank.controller;

import com.don.bank.dto.AccountDTO;
import com.don.bank.service.AccountService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private AccountService accountService;
    private ClientService clientService;

    public AccountController(AccountService accountService, ClientService clientService) {
        this.accountService = accountService;
        this.clientService = clientService;
    }

    @Operation(summary = "Retrieve account list", description = "Returns all accounts or accounts of a specific client by clientId.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity getAccounts(
            @Parameter(description = "Client ID for filtering accounts", required = false)
            @RequestParam(value = "cid", required = false) Long clientId) {
        try {
            if (clientId == null) {
                return ResponseEntity.ok(accountService.getAllAccounts());
            }
            return ResponseEntity.ok(accountService.getAccountsByClientId(clientId));
        } catch (Exception e) {
            log.error("Error getting accounts", e);
            return new ResponseEntity("Error getting accounts", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create an account", description = "Creates a new account for a client.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity createAccount(
            @Parameter(description = "Account data to create", required = true)
            @RequestBody AccountDTO accountDTO) {
        if (accountDTO.getClientId() == 0) {
            log.warn("Client id is required");
            return new ResponseEntity("Client id is required", HttpStatus.BAD_REQUEST);
        }

        try {
            clientService.getById(accountDTO.getClientId());
            return ResponseEntity.ok(accountService.createAccount(accountDTO));
        } catch (EntityNotFoundException e) {
            log.warn("Client not found: {}", accountDTO.getClientId());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error creating account", e);
            return new ResponseEntity("Error creating account", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update an account", description = "Updates the details of an existing account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account successfully updated"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public ResponseEntity updateAccount(
            @Parameter(description = "Updated account data", required = true)
            @RequestBody AccountDTO accountDTO) {
        try {
            if (accountService.getAccountById(accountDTO.getId()) == null) {
                throw new EntityNotFoundException("Account not found");
            }

            return ResponseEntity.ok(accountService.updateAccount(accountDTO));
        } catch (EntityNotFoundException e) {
            log.error("Error updating account: ", e);
            return new ResponseEntity<>("Error updating account: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error updating account", e);
            return new ResponseEntity<>("Error updating account", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete an account", description = "Deletes an existing account by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteAccount(
            @Parameter(description = "ID of the account to delete", required = true)
            @PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.ok("Account deleted " + id);
        } catch (IllegalArgumentException e) {
            log.error("Error deleting account: ", e);
            return new ResponseEntity("Error deleting account: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error deleting account: ", e);
            return new ResponseEntity("Error deleting account", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Withdraw funds", description = "Withdraws the specified amount from the account balance.")
    @PostMapping("/{id}/withdraw")
    public ResponseEntity withdraw(
            @Parameter(description = "ID of the account", required = true)
            @PathVariable Long id,
            @Parameter(description = "Amount to withdraw", required = true)
            @RequestBody double amount) {
        try {
            return ResponseEntity.ok(accountService.withdrawBalance(id, amount));
        } catch (IllegalArgumentException e) {
            log.error("Error withdrawing balance: ", e);
            return new ResponseEntity("Error withdrawing balance: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error withdrawing balance: ", e);
            return new ResponseEntity("Error withdrawing balance", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deposit funds", description = "Deposits the specified amount into the account balance.")
    @PostMapping("/{id}/deposit")
    public ResponseEntity deposit(
            @Parameter(description = "ID of the account", required = true)
            @PathVariable Long id,
            @Parameter(description = "Amount to deposit", required = true)
            @RequestBody double amount) {
        try {
            return ResponseEntity.ok(accountService.depositBalance(id, amount));
        } catch (IllegalArgumentException e) {
            log.error("Error depositing balance: ", e);
            return new ResponseEntity("Error depositing balance: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error depositing balance: ", e);
            return new ResponseEntity("Error depositing balance", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
