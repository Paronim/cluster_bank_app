package com.don.bank.controller;

import com.don.bank.dto.TransactionDTO;
import com.don.bank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Retrieve transactions",
            description = "Fetch all transactions or filter by sender and recipient account IDs."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transactions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity getTransactions(
            @Parameter(description = "Sender account ID to filter transactions") @RequestParam(value = "sid", required = false) Long senderId,
            @Parameter(description = "Recipient account ID to filter transactions") @RequestParam(value = "rid", required = false) Long receiverId) {
        try {
            if (senderId != null && receiverId != null) {
                return ResponseEntity.ok(transactionService.getTransactionsBySenderAndRecipientId(senderId, receiverId));
            }
            return ResponseEntity.ok(transactionService.getAllTransactions());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Error retrieving transactions: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Retrieve a specific transaction",
            description = "Fetch transaction details by its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transaction"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity getTransaction(
            @Parameter(description = "Unique identifier of the transaction", required = true) @PathVariable Long id) {
        try {
            return ResponseEntity.ok(transactionService.getTransactionById(id));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Error retrieving transaction by ID: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Transfer money",
            description = "Initiates a money transfer between accounts."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully transferred money"),
            @ApiResponse(responseCode = "400", description = "Bad request, e.g., invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/transfer")
    public ResponseEntity transfer(
            @Parameter(description = "Transaction details for the money transfer", required = true) @RequestBody TransactionDTO transactionDTO) {
        try {
            transactionService.transferMoney(transactionDTO);
            return ResponseEntity.ok("Money transferred successfully");
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity("Error transferring money: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Error transferring money: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
