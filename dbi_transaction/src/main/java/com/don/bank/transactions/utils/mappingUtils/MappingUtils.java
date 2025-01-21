package com.don.bank.transactions.utils.mappingUtils;

import com.don.common.models.ReceiptDTO;
import com.don.bank.transactions.entity.Receipt;
import com.don.bank.transactions.entity.Transaction;

import java.sql.Timestamp;

public class MappingUtils {

    public static Receipt mapToReceipt(ReceiptDTO receiptDTO) {

        return Receipt.builder()
                .id(receiptDTO.getId())
                .amount(receiptDTO.getAmount())
                .transactionType(Receipt.TransactionType.valueOf(receiptDTO.getTransactionType()))
                .createdAt(receiptDTO.getCreatedAt().getTime())
                .accountId(receiptDTO.getAccountId())
                .recipientId(receiptDTO.getRecipientId())
                .build();
    }

    public static ReceiptDTO mapToReceiptDTO(Receipt Receipt) {

        return ReceiptDTO.builder()
                .id(Receipt.getId())
                .amount(Receipt.getAmount())
                .transactionType(Receipt.getTransactionType().name())
                .createdAt(new Timestamp(Receipt.getCreatedAt()))
                .accountId(Receipt.getAccountId())
                .recipientId(Receipt.getRecipientId())
                .build();
    }

    public static ReceiptDTO mapToReceiptDTO(Transaction transaction) {

        return ReceiptDTO.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType().name())
                .createdAt(transaction.getCreatedAt())
                .accountId(transaction.getAccountId())
                .recipientId(transaction.getRecipientId())
                .build();
    }

    public static Transaction mapToTransaction(ReceiptDTO receiptDTO) {

        return Transaction.builder()
                .id(receiptDTO.getId())
                .amount(receiptDTO.getAmount())
                .transactionType(Transaction.TransactionType.valueOf(receiptDTO.getTransactionType()))
                .createdAt(new Timestamp(receiptDTO.getCreatedAt().getTime()))
                .accountId(receiptDTO.getAccountId())
                .recipientId(receiptDTO.getRecipientId())
                .build();
    }
}
