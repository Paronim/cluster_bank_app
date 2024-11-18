package com.don.bank.service;

import com.don.bank.dto.AccountDTO;
import com.don.bank.entity.Account;
import com.don.bank.entity.Client;
import com.don.bank.entity.Transaction;
import com.don.bank.repository.AccountRepository;
import com.don.bank.util.mappingUtils.MappingUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository,@Lazy TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;


    }

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream().map(MappingUtils::mapToAccountDto).toList();
    }

    public List<AccountDTO> getAccountsByClientId(long id) {
        return accountRepository.findByClientId(id).stream().map(MappingUtils::mapToAccountDto).toList();
    }

    public List<Account> getAccountsByClientIdEntity(long id) {
        return accountRepository.findByClientId(id);
    }

    public Account getAccountById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account createAccount(String currency, double balance, String name, String type, Client client) {
        return accountRepository.save(Account.builder()
                .currency(Account.Currency.valueOf(currency))
                .balance(balance)
                .name(name)
                .type(Account.Type.valueOf(type))
                .client(client)
                .build());
    }

    public AccountDTO createAccount(AccountDTO accountDTO) {

        List<Account> accounts = getAccountsByClientIdEntity(accountDTO.getClientId());

        if (accountDTO.getBalance() > 0) {
            accountDTO.setBalance(0d);
        }

        if(accounts.stream().filter(a -> a.getType().equals(Account.Type.main)).toList().isEmpty()){
            accountDTO.setType("main");
        } else {
            accountDTO.setType("secondary");
        }

        Account account = MappingUtils.mapToAccount(accountDTO);

        return MappingUtils.mapToAccountDto(accountRepository.save(account));
    }

    @Transactional
    public AccountDTO updateAccount(AccountDTO accountDTO) {

        Account oldAccount = getAccountById(accountDTO.getId());

        if(accountDTO.getType().equals("secondary") && oldAccount.getType().equals(Account.Type.main)){
            throw new IllegalArgumentException("type can't be main because account is secondary");
        }

        if(accountDTO.getBalance() != 0 && accountDTO.getBalance() != oldAccount.getBalance()){
            throw new IllegalArgumentException("balance can't updated this method");
        } else if(accountDTO.getBalance() == 0) {
            accountDTO.setBalance(oldAccount.getBalance());
        }

        if(Objects.equals(accountDTO.getType(), "main")) {
            List<Account> accounts = getAccountsByClientIdEntity(oldAccount.getClient().getId());
            Account mainAccount = accounts.stream().filter(a -> a.getType().equals(Account.Type.main)).toList().getFirst();
            mainAccount.setType(Account.Type.secondary);
            accountRepository.save(mainAccount);
        }

        accountDTO.setClientId(oldAccount.getClient().getId());
        Account newAccount = MappingUtils.mapToAccount(accountDTO);

        return MappingUtils.mapToAccountDto(accountRepository.save(newAccount));
    }

    public void deleteAccount(long id) {

        Account account = getAccountById(id);

        if(account.getType().equals(Account.Type.main)){
            throw new IllegalArgumentException("can't delete main account");
        }

        accountRepository.deleteById(id);
    }

    @Transactional
    public AccountDTO withdrawBalance(long id, double amount) {
        Account account = getAccountById(id);

        if(account == null){
            throw new IllegalArgumentException("account not found");
        }

        if(account.getBalance() < amount){
            throw new IllegalArgumentException("not enough money");
        }

        account.setBalance(account.getBalance() - amount);

        transactionService.createTransaction(Transaction.builder()
                .amount(amount)
                .transactionType(Transaction.TransactionType.WITHDRAW)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .account(Account.builder().id(id).build())
                .build());

        return MappingUtils.mapToAccountDto(accountRepository.save(account));
    }

    @Transactional
    public AccountDTO depositBalance(long id, double amount) {
        Account account = getAccountById(id);

        if(account == null){
            throw new IllegalArgumentException("account not found");
        }

        account.setBalance(account.getBalance() + amount);

        transactionService.createTransaction(Transaction.builder()
                .amount(amount)
                .transactionType(Transaction.TransactionType.DEPOSIT)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .account(Account.builder().id(id).build())
                .build());

        return MappingUtils.mapToAccountDto(accountRepository.save(account));
    }

}
