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

/**
 * Service for managing transactions. Handles operations such as retrieving, creating,
 * and transferring money between accounts, including currency conversion when needed.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ConvertorCurrencyService convertorCurrencyService;
    private final AccountService accountService;

    /**
     * Constructs the TransactionService with necessary dependencies.
     *
     * @param transactionRepository the repository for managing transaction data
     * @param convertorCurrencyService the service for currency conversion
     * @param accountService the service for managing accounts
     */
    public TransactionService(TransactionRepository transactionRepository,
                              ConvertorCurrencyService convertorCurrencyService,
                              @Lazy AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.convertorCurrencyService = convertorCurrencyService;
        this.accountService = accountService;
    }

    /**
     * Retrieves all transactions.
     *
     * @return a list of {@link TransactionDTO} representing all transactions
     */
    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(MappingUtils::mapToTransactionDto)
                .toList();
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param id the unique identifier of the transaction
     * @return a {@link TransactionDTO} representing the transaction, or null if not found
     */
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        return MappingUtils.mapToTransactionDto(transaction);
    }

    /**
     * Creates and saves a new transaction.
     *
     * @param transaction the {@link Transaction} to be created
     */
    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    /**
     * Retrieves transactions filtered by sender and recipient account IDs.
     *
     * @param senderId the ID of the sender's account
     * @param recipientId the ID of the recipient's account
     * @return a list of {@link TransactionDTO} matching the criteria
     */
    public List<TransactionDTO> getTransactionsBySenderAndRecipientId(long senderId, long recipientId) {
        return transactionRepository.findByAccountAndRecipient(senderId, recipientId)
                .stream()
                .map(MappingUtils::mapToTransactionDto)
                .toList();
    }

    /**
     * Transfers money between two accounts. Handles account balance updates
     * and currency conversion if the accounts have different currencies.
     *
     * @param transactionDTO the details of the transaction, including sender, recipient, and amount
     * @throws IllegalArgumentException if either account is not found
     */
    @Transactional
    public void transferMoney(TransactionDTO transactionDTO) {
        transactionDTO.setTransactionType(String.valueOf(Transaction.TransactionType.TRANSFER));
        Transaction transaction = MappingUtils.mapToTransaction(transactionDTO);

        Account accountSender = accountService.getAccountById(transaction.getAccount().getId());
        Account accountRecipient = accountService.getAccountById(transaction.getRecipient().getId());

        if (accountSender == null || accountRecipient == null) {
            throw new IllegalArgumentException("Account not found");
        }

        accountService.withdrawBalance(transaction.getAccount().getId(), transaction.getAmount());

        if (!accountSender.getCurrency().equals(accountRecipient.getCurrency())) {
            transaction.setAmount(convertorCurrencyService.convert(
                    transaction.getAmount(),
                    String.valueOf(accountSender.getCurrency()),
                    String.valueOf(accountRecipient.getCurrency())));
        }

        transaction.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        accountService.depositBalance(transaction.getRecipient().getId(), transaction.getAmount());

        transactionRepository.save(transaction);
    }
}
