package com.don.bank.service;

import com.don.bank.dto.TransactionDTO;
import com.don.bank.entity.Account;
import com.don.bank.entity.Transaction;
import com.don.bank.repository.TransactionRepository;
import com.don.bank.util.mappingUtils.MappingUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ConvertorCurrencyService convertorCurrencyService;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, ConvertorCurrencyService convertorCurrencyService,@Lazy AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.convertorCurrencyService = convertorCurrencyService;
        this.accountService = accountService;
    }

    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream().map(MappingUtils::mapToTransactionDto).toList();
    }

    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);

        return MappingUtils.mapToTransactionDto(transaction);
    }

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<TransactionDTO> getTransactionsBySenderAndRecipientId (long senderId, long recipientId) {
        return transactionRepository.findByAccountAndRecipient(senderId, recipientId).stream().map(MappingUtils::mapToTransactionDto).toList();
    }

    @Transactional
    public void transferMoney(TransactionDTO transactionDTO) {

        transactionDTO.setTransactionType(String.valueOf(Transaction.TransactionType.TRANSFER));
        Transaction transaction = MappingUtils.mapToTransaction(transactionDTO);

        Account accountSender = accountService.getAccountById(transaction.getAccount().getId());
        Account accountRecipient = accountService.getAccountById(transaction.getRecipient().getId());

        if(accountSender == null || accountRecipient == null) {
            throw new IllegalArgumentException("Account not found");
        }

        accountService.withdrawBalance(transaction.getAccount().getId(), transaction.getAmount());

        if(!accountSender.getCurrency().equals(accountRecipient.getCurrency())) {
            transaction.setAmount(convertorCurrencyService.convert(transaction.getAmount(),
                    String.valueOf(accountSender.getCurrency()),
                    String.valueOf(accountRecipient.getCurrency())));
        }

        transaction.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        accountService.depositBalance(transaction.getRecipient().getId(), transaction.getAmount());

        transactionRepository.save(transaction);
    }
}
