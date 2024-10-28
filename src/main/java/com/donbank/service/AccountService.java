package com.donbank.service;

import com.donbank.entity.Account;
import com.donbank.exception.AccountNotFoundException;
import com.donbank.exception.InsufficientFundsException;
import com.donbank.repository.AccountRepository;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * AccountService manages operations related to accounts,
 * such as depositing funds, creating accounts, and deleting accounts.
 * It interacts with the AccountRepository to retrieve and manipulate account data.
 */
public class AccountService {

    /**
     * Enum representing the available currencies for accounts.
     */
    enum Currency {
        RUB,
        USD
    }

    private AccountRepository accountRepository;

    /**
     * Initializes a new instance of AccountService,
     * creating a new AccountRepository.
     */
    public AccountService() {
        this.accountRepository = new AccountRepository();
    }

    /**
     * Initializes a new instance of AccountService with a provided AccountRepository.
     * This constructor allows for dependency injection of an existing AccountRepository.
     *
     * @param accountRepository an existing instance of AccountRepository.
     */
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Retrieves a list of accounts associated with the specified client ID.
     *
     * @param id the unique identifier of the client whose accounts are to be retrieved.
     * @return a list of Account objects associated with the specified client ID.
     */
    public List<Account> getAccountsByIdClient(int id) {
        return accountRepository.getAccountsByIdClient(id);
    }

    /**
     * Deposits funds into or withdraws funds from an account,
     * depending on the specified action parameter.
     *
     * @param currency the currency of the account.
     * @param amount the amount of money to deposit or withdraw.
     * @param accounts the list of accounts associated with the client.
     * @param param specifies whether to contribute (withdraw) or not (deposit).
     * @return a message indicating the outcome of the operation.
     * @throws InsufficientFundsException if attempting to withdraw more than the account balance.
     */
    public String depositFunds(String currency, int amount, List<Account> accounts, String param) throws InsufficientFundsException {
        if (!isValidCurrency(currency)) {
            return "Not valid currency";
        }
        boolean checkParam = Objects.equals(param, "contribute");
        Account account = getAccountsByCurrency(currency, accounts);

        if (checkParam && account.getBalance() < amount) {
            throw new InsufficientFundsException();
        }

        double balance = Objects.equals(param, "contribute") ? account.getBalance() - amount : account.getBalance() + amount;
        account.setBalance(balance);

        return "Amount updated";
    }

    /**
     * Creates a new account with the specified currency for the client.
     *
     * @param currency the currency of the new account.
     * @param accounts the list of accounts associated with the client.
     * @param clientId the unique identifier of the client.
     * @return a message indicating the outcome of the operation.
     */
    public String createAccount(String currency, List<Account> accounts, int clientId) {
        if (!isValidCurrency(currency)) {
            return "Not valid currency";
        }
        boolean accountExists = accounts.stream().anyMatch(a -> Objects.equals(a.getCurrency(), currency));
        if (!accountExists) {
            accounts.add(new Account(accountRepository.getAccountsData().size() + 1, currency.toUpperCase(), 0, clientId));
            return "account created";
        }
        return "an account with this currency already exists";
    }

    /**
     * Deletes an account with the specified currency from the client's account list.
     *
     * @param currency the currency of the account to be deleted.
     * @param accounts the list of accounts associated with the client.
     * @return a message indicating the outcome of the operation.
     * @throws AccountNotFoundException if no account with the specified currency is found.
     */
    public String deleteAccount(String currency, List<Account> accounts) throws AccountNotFoundException {
        if (!isValidCurrency(currency)) {
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

    /**
     * Retrieves the account associated with the specified currency from the client's account list.
     *
     * @param currency the currency of the account to retrieve.
     * @param accounts the list of accounts associated with the client.
     * @return the Account object associated with the specified currency.
     */
    private Account getAccountsByCurrency(String currency, List<Account> accounts) {
        return accounts.stream().filter(a -> Objects.equals(a.getCurrency(), currency)).findFirst().orElse(null);
    }

    /**
     * Validates whether the provided currency is supported.
     *
     * @param currency the currency to validate.
     * @return true if the currency is valid, false otherwise.
     */
    private boolean isValidCurrency(String currency) {
        return Arrays.stream(Currency.values()).anyMatch(c -> c.name().equalsIgnoreCase(currency));
    }
}