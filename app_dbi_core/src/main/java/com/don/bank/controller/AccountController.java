package com.don.bank.controller;

import com.don.bank.dto.AccountDTO;
import com.don.bank.exception.account.AccountNotFoundException;
import com.don.bank.exception.account.ErrorDeleteAccountException;
import com.don.bank.exception.client.ClientNotFoundException;
import com.don.bank.exception.transaction.MoneyTransactionException;
import com.don.bank.service.AccountService;
import com.don.bank.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    private AccountService accountService;

    private ClientService clientService;

    private final MessageSource messageSource;

    public AccountController(AccountService accountService, ClientService clientService, MessageSource messageSource) {
        this.accountService = accountService;
        this.clientService = clientService;
        this.messageSource = messageSource;
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
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Error getting accounts"));
        }
    }

    @Operation(summary = "Retrieve a account by ID", description = "Fetches a account by their unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity getAccountById(@Parameter(description = "Unique identifier of the account", required = true) @PathVariable Long id) {
        try {
            return ResponseEntity.ok(accountService.getAccountDTOById(id));
        } catch (AccountNotFoundException e) {
            log.warn("Account not found: {}", id);
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error getting accounts", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Error getting account"));
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
            @RequestBody AccountDTO accountDTO,
            Locale locale) {
        if (accountDTO.getClientId() == 0) {
            log.warn("Client id is required");
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Client id is required"));
        }

        try {
            clientService.getById(accountDTO.getClientId());
            accountService.createAccount(accountDTO);
            return ResponseEntity.ok(Map.of(
                    "message", messageSource.getMessage("message.account.create.ok", null, locale)));
        } catch (ClientNotFoundException e) {
            log.warn("Client not found: {}", accountDTO.getClientId());
            return new ResponseEntity<>(Map.of("message", messageSource.getMessage("message.client.notFoundError", null, locale)), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error creating account", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Error creating account"));
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
            @RequestBody AccountDTO accountDTO,
            Locale locale) {
        try {
            if (accountService.getAccountById(accountDTO.getId()) == null) {
                throw new AccountNotFoundException();
            }

            accountService.updateAccount(accountDTO);

            return ResponseEntity.ok(Map.of("message", messageSource.getMessage("message.account.update.ok", null, locale)));
        } catch (AccountNotFoundException e) {
            log.error("Error updating account: ", e);
            return new ResponseEntity<>(Map.of("message", messageSource.getMessage("message.account.notFoundError", null, locale)), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error updating account", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", messageSource.getMessage("message.account.error.update", null, locale)));
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
            @PathVariable Long id,
            Locale locale) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.ok(Map.of("message", messageSource.getMessage("message.account.delete.ok", null, locale)));
        } catch (AccountNotFoundException e) {
            log.error("Account not found: ", e);
            return new ResponseEntity<>(Map.of("message", messageSource.getMessage("message.account.notFoundError", null, locale)), HttpStatus.NOT_FOUND);
        } catch (ErrorDeleteAccountException e) {
            log.error("Error deleting account: ", e);
            return ResponseEntity.badRequest().body(Map.of("message", "Error deleting account: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting account: ", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Error deleting account"));
        }
    }

    @Operation(summary = "Withdraw funds", description = "Withdraws the specified amount from the account balance.")
    @PostMapping("/{id}/withdraw")
    public ResponseEntity withdraw(
            @Parameter(description = "ID of the account", required = true)
            @PathVariable Long id,
            @Parameter(description = "Amount to withdraw", required = true)
            @RequestBody Map<String, Double> withdraw,
            Locale locale) {
        try {
            accountService.withdrawBalance(id, withdraw.get("amount"), "transaction");
            return ResponseEntity.ok(Map.of("message", messageSource.getMessage("message.account.withdraw.ok", null, locale)));
        } catch (AccountNotFoundException e) {
            log.error("Account not found: ", e);
            return new ResponseEntity<>(Map.of("message", messageSource.getMessage("message.account.notFoundError", null, locale)), HttpStatus.NOT_FOUND);
        } catch (MoneyTransactionException e) {
            log.error("Error money transaction: ", e);
            return ResponseEntity.badRequest().body(Map.of("message", messageSource.getMessage("message.account.error.withdraw", null, locale)));
        } catch (Exception e) {
            log.error("Error withdrawing balance: ", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Error withdrawing balance"));
        }
    }

    @Operation(summary = "Deposit funds", description = "Deposits the specified amount into the account balance.")
    @PostMapping("/{id}/deposit")
    public ResponseEntity deposit(
            @Parameter(description = "ID of the account", required = true)
            @PathVariable Long id,
            @Parameter(description = "Amount to deposit", required = true)
            @RequestBody Map<String, Double> deposit,
            Locale locale) {
        try {
            accountService.depositBalance(id, deposit.get("amount"), "transaction");
            return ResponseEntity.ok(Map.of("message", messageSource.getMessage("message.account.deposit.ok", null, locale)));
        } catch (AccountNotFoundException e) {
            log.error("Account not found: ", e);
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error depositing balance: ", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Error depositing balance"));
        }
    }
}
