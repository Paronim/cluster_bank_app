package com.don.bank.transactions.service;

import com.don.bank.transactions.dto.ReceiptDTO;
import com.don.bank.transactions.entity.Transaction;
import com.don.bank.transactions.repository.TransactionRepository;
import com.don.bank.transactions.utils.mappingUtils.MappingUtils;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void save(ReceiptDTO receiptDTO) {
        Transaction transaction = MappingUtils.mapToTransaction(receiptDTO);
        transactionRepository.save(transaction);
    }
}
