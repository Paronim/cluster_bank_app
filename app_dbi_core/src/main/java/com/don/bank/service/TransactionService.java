package com.don.bank.service;

import com.don.bank.dto.TransactionDTO;
import com.don.bank.entity.Account;
import com.don.bank.entity.Transaction;
import com.don.bank.repository.TransactionRepository;
import com.don.bank.util.mappingUtils.MappingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class responsible for managing transactions within the banking system.
 * It handles operations such as retrieving, creating, transferring money between accounts,
 * and converting currencies when necessary. It interacts with various repositories
 * and services to perform these operations.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ConvertorCurrencyService convertorCurrencyService;
    private final AccountService accountService;
    private final ProducerService producerService;
    private final RestClient restClient;

    @Value("${transactions.service.api.url}")
    private String receiptUrl;

    /**
     * Constructs the {@link TransactionService} with the necessary dependencies.
     *
     * @param transactionRepository the repository responsible for managing transaction data
     * @param convertorCurrencyService the service responsible for currency conversion
     * @param accountService the service responsible for managing accounts
     * @param producerService the service for producing messages related to transactions
     * @param restClient the HTTP client for making external API calls
     */
    public TransactionService(TransactionRepository transactionRepository,
                              ConvertorCurrencyService convertorCurrencyService,
                              @Lazy AccountService accountService,
                              ProducerService producerService,
                              RestClient restClient) {
        this.transactionRepository = transactionRepository;
        this.convertorCurrencyService = convertorCurrencyService;
        this.accountService = accountService;
        this.producerService = producerService;
        this.restClient = restClient;
    }

    /**
     * Retrieves all transactions from the external transaction service.
     *
     * @return a list of {@link TransactionDTO} representing all transactions
     * @throws HttpClientErrorException if the service call fails
     */
    public List<TransactionDTO> getAllTransactions() {
        ResponseEntity<List<TransactionDTO>> response = restClient.get()
                .uri(receiptUrl  + "/" + "all")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<TransactionDTO>>() {});

        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new HttpClientErrorException(response.getStatusCode(), "Error service transactions");
        }
    }

    /**
     * Retrieves a transaction by its unique identifier.
     *
     * @param id the unique identifier of the transaction
     * @return a {@link TransactionDTO} representing the transaction, or null if not found
     * @throws HttpClientErrorException if the service call fails
     */
    public TransactionDTO getTransactionById(Long id) {
        ResponseEntity<TransactionDTO> response = restClient.get()
                .uri(receiptUrl + "/" + id)
                .retrieve()
                .toEntity(TransactionDTO.class);

        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new HttpClientErrorException(response.getStatusCode(), "Error service transactions");
        }
    }

    /**
     * Creates and saves a new transaction.
     *
     * @param transaction the {@link Transaction} to be created
     * @throws IllegalArgumentException if the transaction cannot be created
     */
    @Transactional
    public void createTransaction(Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);

        producerService.send(MappingUtils.mapToTransactionDto(savedTransaction));
    }

    /**
     * Retrieves transactions filtered by sender and recipient account IDs.
     *
     * @param senderId the ID of the sender's account
     * @param recipientId the ID of the recipient's account (maybe null)
     * @return a list of {@link TransactionDTO} matching the criteria
     * @throws HttpClientErrorException if the service call fails
     */
    public List<TransactionDTO> getTransactionsBySenderAndRecipientId(long senderId, Long recipientId) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(receiptUrl)
                .queryParam("sid", senderId)
                .queryParam("rid", recipientId)
                .build();

        ResponseEntity<List<TransactionDTO>> response = restClient.get()
                .uri(uriComponents.toUri())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<TransactionDTO>>() {});

        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new HttpClientErrorException(response.getStatusCode(), "Error service transactions");
        }
    }

    /**
     * Transfers money between two accounts, ensuring balance updates and currency conversion if needed.
     * The method performs the following steps:
     * <ul>
     *   <li>Withdraws the transaction amount from the sender's account</li>
     *   <li>Converts the amount if the sender's and recipient's currencies differ</li>
     *   <li>Deposits the amount into the recipient's account</li>
     *   <li>Saves the transaction to the repository and sends it to the producer service</li>
     * </ul>
     *
     * @param transactionDTO the details of the transaction, including sender, recipient, and amount
     * @throws IllegalArgumentException if either account is not found or another error occurs during transfer
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

        Transaction savedTransaction = transactionRepository.save(transaction);

        producerService.send(MappingUtils.mapToTransactionDto(savedTransaction));
    }

    /**
     * Transfers transactions from the local database to an external service via REST API.
     * It retrieves all transactions from the repository, converts them to {@link TransactionDTO},
     * and sends them to the external service.
     */
    public void transferTransactionFromElasticsearch() {
        List<Transaction> transactions = transactionRepository.findAll();

        List<TransactionDTO> transactionDTOList = transactions.stream().map(MappingUtils::mapToTransactionDto).toList();

        restClient.post()
                .uri(receiptUrl + "/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(transactionDTOList)
                .retrieve()
                .toBodilessEntity();
    }
}
