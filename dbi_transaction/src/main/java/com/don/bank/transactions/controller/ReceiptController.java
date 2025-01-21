package com.don.bank.transactions.controller;

import com.don.common.models.ReceiptDTO;
import com.don.bank.transactions.service.ReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {
    
    Logger logger = LoggerFactory.getLogger(ConsumerController.class);
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/all")
    public ResponseEntity getAllTransactions() {
        try{
            return ResponseEntity.ok(receiptService.getAllReceipt());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity("Error retrieving receipts: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getTransactionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(receiptService.getReceiptById(id));
        } catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity("Error retrieving receipt: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity getTransactionsBySenderAndRecipientId(@RequestParam(value = "sid") Long accountId, @RequestParam(value = "rid", required = false) Long recipientId){
        try {
            return ResponseEntity.ok(receiptService.getReceiptBySenderAndRecipientId(accountId, recipientId));
        } catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity("Error retrieving receipts: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/all")
    public ResponseEntity saveReceipt(@RequestBody List<ReceiptDTO> receiptDTOList) {
        try {
            receiptService.saveAll(receiptDTOList);
            return ResponseEntity.ok("Save receipts successfully");
        } catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity("Error retrieving receipts: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
