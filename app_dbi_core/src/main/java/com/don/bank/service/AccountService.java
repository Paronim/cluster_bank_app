package com.don.bank.service;

import com.don.bank.dto.AccountDTO;
import com.don.bank.entity.Account;
import com.don.bank.entity.Client;
import com.don.bank.entity.Transaction;
import com.don.bank.repository.AccountRepository;
import com.don.bank.util.mappingUtils.MappingUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Service for managing accounts. Provides operations for creating, retrieving, updating,
 * and deleting accounts, as well as performing transactions like deposits and withdrawals.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    /**
     * Constructs the AccountService with dependencies.
     *
     * @param accountRepository the repository for account persistence
     * @param transactionService the service for managing transactions (lazy-loaded to avoid circular dependency)
     */
    public AccountService(AccountRepository accountRepository, @Lazy TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    /**
     * Retrieves all accounts.
     *
     * @return a list of all accounts as DTOs
     */
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(MappingUtils::mapToAccountDto)
                .toList();
    }

    /**
     * Retrieves accounts associated with a specific client by client ID.
     *
     * @param id the client ID
     * @return a list of the client's accounts as DTOs
     */
    public List<AccountDTO> getAccountsByClientId(long id) {
        return accountRepository.findByClientId(id).stream()
                .map(MappingUtils::mapToAccountDto)
                .toList();
    }

    /**
     * Retrieves accounts associated with a specific client by client ID as entities.
     *
     * @param id the client ID
     * @return a list of the client's accounts as entities
     */
    public List<Account> getAccountsByClientIdEntity(long id) {
        return accountRepository.findByClientId(id);
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param id the account ID
     * @return the account entity, or null if not found
     */
    public Account getAccountById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param id the account ID
     * @return the account dto entity, or null if not found
     */
    public AccountDTO getAccountDTOById(long id) {
        Account account = accountRepository.findById(id).orElse(null);

        if (account == null) {
            throw new EntityNotFoundException("Account with id " + id + " not found");
        }

        return MappingUtils.mapToAccountDto(account);
    }
    /**
     * Creates a new account.
     *
     * @param currency the currency of the account
     * @param balance the initial balance
     * @param name the name of the account
     * @param type the type of account (main or secondary)
     * @param client the associated client
     * @return the created account entity
     */
    public Account createAccount(String currency, double balance, String name, String type, Client client) {
        return accountRepository.save(Account.builder()
                .currency(Account.Currency.valueOf(currency))
                .balance(balance)
                .name(name)
                .type(Account.Type.valueOf(type))
                .client(client)
                .build());
    }

    /**
     * Creates a new account using an AccountDTO.
     *
     * @param accountDTO the account details
     * @return the created account as a DTO
     */
    public AccountDTO createAccount(AccountDTO accountDTO) {
        List<Account> accounts = getAccountsByClientIdEntity(accountDTO.getClientId());

        if (accountDTO.getBalance() > 0) {
            accountDTO.setBalance(0d);
        }

        if (accounts.stream().noneMatch(a -> a.getType().equals(Account.Type.main))) {
            accountDTO.setType("main");
        } else {
            accountDTO.setType("secondary");
        }

        Account account = MappingUtils.mapToAccount(accountDTO);
        return MappingUtils.mapToAccountDto(accountRepository.save(account));
    }

    /**
     * Updates an existing account.
     *
     * @param accountDTO the updated account details
     * @return the updated account as a DTO
     */
    @Transactional
    public AccountDTO updateAccount(AccountDTO accountDTO) {
        Account oldAccount = getAccountById(accountDTO.getId());

        if (accountDTO.getType().equals("secondary") && oldAccount.getType().equals(Account.Type.main)) {
            throw new IllegalArgumentException("type can't be main because account is secondary");
        }

        if (accountDTO.getBalance() != 0 && accountDTO.getBalance() != oldAccount.getBalance()) {
            throw new IllegalArgumentException("balance can't be updated with this method");
        } else if (accountDTO.getBalance() == 0) {
            accountDTO.setBalance(oldAccount.getBalance());
        }

        if (Objects.equals(accountDTO.getType(), "main")) {
            List<Account> accounts = getAccountsByClientIdEntity(oldAccount.getClient().getId());
            Account mainAccount = accounts.stream()
                    .filter(a -> a.getType().equals(Account.Type.main))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Main account not found"));
            mainAccount.setType(Account.Type.secondary);
            accountRepository.save(mainAccount);
        }

        accountDTO.setClientId(oldAccount.getClient().getId());
        Account newAccount = MappingUtils.mapToAccount(accountDTO);
        return MappingUtils.mapToAccountDto(accountRepository.save(newAccount));
    }

    /**
     * Deletes an account by its ID.
     *
     * @param id the account ID
     */
    public void deleteAccount(long id) {
        Account account = getAccountById(id);

        if (account.getType().equals(Account.Type.main)) {
            throw new IllegalArgumentException("can't delete main account");
        }

        accountRepository.deleteById(id);
    }

    /**
     * Withdraws a specific amount from an account.
     *
     * @param id the account ID
     * @param amount the amount to withdraw
     * @return the updated account as a DTO
     */
    @Transactional
    public AccountDTO withdrawBalance(long id, double amount, String... type) {
        Account account = getAccountById(id);

        if (account == null) {
            throw new IllegalArgumentException("account not found");
        }

        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("not enough money");
        }

        account.setBalance(account.getBalance() - amount);

        if(type.length != 0) {
            transactionService.createTransaction(Transaction.builder()
                    .amount(amount)
                    .transactionType(Transaction.TransactionType.WITHDRAW)
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .account(Account.builder().id(id).build())
                    .build());
        }

        return MappingUtils.mapToAccountDto(accountRepository.save(account));
    }

    /**
     * Deposits a specific amount into an account.
     *
     * @param id the account ID
     * @param amount the amount to deposit
     * @return the updated account as a DTO
     */
    @Transactional
    public AccountDTO depositBalance(long id, double amount, String... type) {
        Account account = getAccountById(id);

        if (account == null) {
            throw new IllegalArgumentException("account not found");
        }

        account.setBalance(account.getBalance() + amount);

        if(type.length != 0) {
            transactionService.createTransaction(Transaction.builder()
                    .amount(amount)
                    .transactionType(Transaction.TransactionType.DEPOSIT)
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .account(Account.builder().id(id).build())
                    .build());
        }

        return MappingUtils.mapToAccountDto(accountRepository.save(account));
    }
}
