package com.donbank.service;

import com.donbank.entity.Account;
import com.donbank.exception.AccountNotFoundException;
import com.donbank.exception.InsufficientFundsException;
import com.donbank.repository.AccountRepository;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class AccountService {

    enum Currency {
        RUB,
        USD
    }

    private AccountRepository accountRepository;

    public AccountService() {
        this.accountRepository = new AccountRepository();
    }

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> getAccountsByIdClient(int id) {
        return accountRepository.getAccountsByIdClient(id);
    }

    public String depositFunds(String currency, int amount, List<Account> accounts, String param) throws InsufficientFundsException {
        if(!isValidCurrency(currency)){
            return "Not valid currency";
        }
        boolean checkParam = Objects.equals(param, "contribute");
        Account account = getAccountsByCurrency(currency, accounts);

        if(checkParam && account.getBalance() < amount){
            throw new InsufficientFundsException();
        }

        double balance = Objects.equals(param, "contribute") ? account.getBalance() - amount : account.getBalance() + amount;
        account.setBalance(balance);

        return "Amount updated";
    }

    public String createAccount(String currency, List<Account> accounts, int clientId) {
        if(!isValidCurrency(currency)){
            return "Not valid currency";
        }
        boolean account = accounts.stream().anyMatch(a -> Objects.equals(a.getCurrency(), currency));
        if(!account){
            accounts.add(new Account(accountRepository.getAccountsData().size()+1, currency.toUpperCase(), 0, clientId));
            return "account created";
        }
        return "an account with this currency already exists";
    }

    public String deleteAccount(String currency, List<Account> accounts) throws AccountNotFoundException {
        if(!isValidCurrency(currency)){
            return "Not valid currency";
        }
        Iterator<Account> iterator = accounts.iterator();

        while (iterator.hasNext()) {
            Account account = iterator.next();
            if (Objects.equals(account.getCurrency(), currency)) {
                iterator.remove();
                return "Account removed";
            }
        }

        throw new AccountNotFoundException();
    }

    private Account getAccountsByCurrency(String currency, List<Account> accounts) {
        return accounts.stream().filter(a -> Objects.equals(a.getCurrency(), currency)).toList().getFirst();
    }

    private boolean isValidCurrency (String currency){
        return Arrays.stream(Currency.values()).anyMatch(c -> c.name().equalsIgnoreCase(currency));
    }
}
