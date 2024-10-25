package com.donbank.service;

import com.donbank.entity.Account;
import com.donbank.repository.AccountRepository;

import java.util.List;

public class AccountService {

    private AccountRepository accountRepository;

    public AccountService() {
        this.accountRepository = new AccountRepository();
    }

    public List<Account> getAccountsByIdClient(int id){
        return accountRepository.getAccountsByIdClient(id);
    }
}
