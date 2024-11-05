package com.donbank.service;

import com.donbank.config.Config;
import com.donbank.entity.Account;
import com.donbank.exception.AccountNotFoundException;
import com.donbank.exception.InsufficientFundsException;
import com.donbank.repository.AccountRepository;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * AccountService manages operations related to accounts,
 * including depositing funds, creating accounts, and deleting accounts.
 * It interacts with the AccountRepository to retrieve and manipulate account data.
 */
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Initializes a new instance of AccountService,
     * creating a new AccountRepository instance.
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
     * @throws SQLException if a database access error occurs.
     */
    public List<Account> getAccountsByIdClient(int id) throws SQLException {
        return accountRepository.getAccountsByIdClient(id);
    }

    /**
     * Modifies the balance of an account by depositing or withdrawing funds based on the specified action.
     *
     * @param currency the currency of the account.
     * @param amount   the amount of money to deposit or withdraw.
     * @param accounts the list of accounts associated with the client.
     * @param param    specifies whether to "withdraw" or "deposit".
     * @return a message indicating the outcome of the operation.
     * @throws InsufficientFundsException if attempting to withdraw more than the account balance.
     * @throws AccountNotFoundException   if no account with the specified currency is found.
     * @throws SQLException               if a database access error occurs.
     */
    public String depositFunds(String currency, double amount, List<Account> accounts, String param)
            throws InsufficientFundsException, AccountNotFoundException, SQLException {
        boolean isWithdraw = Objects.equals(param, "withdraw");
        Account account = getAccountsByCurrency(currency, accounts);

        if (isWithdraw && account.getBalance() < amount) {
            throw new InsufficientFundsException();
        }

        double newBalance = isWithdraw ? account.getBalance() - amount : account.getBalance() + amount;
        accountRepository.updateAccount(account.getId(), String.valueOf(account.getCurrency()), newBalance, account.getClientId());

        Account updatedAccount = accountRepository.getAccount(account.getId());
        account.setBalance(updatedAccount.getBalance());

        return "Amount updated";
    }

    /**
     * Creates a new account with the specified currency for a client if it doesn't already exist.
     *
     * @param currency the currency of the new account.
     * @param accounts the list of accounts associated with the client.
     * @param clientId the unique identifier of the client.
     * @return a message indicating the outcome of the account creation operation.
     * @throws SQLException if a database access error occurs.
     */
    public String createAccount(String currency, List<Account> accounts, int clientId) throws SQLException {
        boolean accountExists = accounts.stream().anyMatch(a -> Objects.equals(String.valueOf(a.getCurrency()), currency));
        if (!accountExists) {
            accounts.add(accountRepository.addAccount(currency.toUpperCase(), 0, clientId));
            return "Account created";
        }
        return "An account with this currency already exists";
    }

    /**
     * Deletes an account with the specified currency from the client's account list.
     *
     * @param currency the currency of the account to be deleted.
     * @param accounts the list of accounts associated with the client.
     * @throws AccountNotFoundException if no account with the specified currency is found.
     * @throws SQLException             if a database access error occurs.
     */
    public void deleteAccount(String currency, List<Account> accounts) throws AccountNotFoundException, SQLException {
        Account account = accounts.stream()
                .filter(a -> Objects.equals(String.valueOf(a.getCurrency()), currency))
                .findFirst()
                .orElseThrow(AccountNotFoundException::new);
        accountRepository.deleteAccount(account.getId());
    }

    /**
     * Retrieves the account associated with the specified currency from the client's account list.
     *
     * @param currency the currency of the account to retrieve.
     * @param accounts the list of accounts associated with the client.
     * @return the Account object associated with the specified currency.
     * @throws AccountNotFoundException if no account with the specified currency is found.
     */
    public Account getAccountsByCurrency(String currency, List<Account> accounts) throws AccountNotFoundException {
        return accounts.stream()
                .filter(a -> Objects.equals(String.valueOf(a.getCurrency()), currency))
                .findFirst()
                .orElseThrow(AccountNotFoundException::new);
    }

    /**
     * Retrieves a list of accounts from a CSV file specified by the given path.
     *
     * @param path the file path to the CSV file.
     * @return a list of accounts read from the CSV file.
     * @throws FileNotFoundException if the specified CSV file is not found.
     */
    public List<Account> getListAccountsCSV(String path) throws FileNotFoundException {
        return accountRepository.getListAccountsFromCSV(path);
    }

    /**
     * Imports accounts from a list, adding each account to the database.
     *
     * @param accounts the list of accounts to be imported.
     * @throws SQLException if a database access error occurs.
     */
    public void importCSVAccounts(List<Account> accounts) throws SQLException {
        for (Account account : accounts) {
            accountRepository.addAccount(String.valueOf(account.getCurrency()), account.getBalance(), account.getClientId());
        }
    }
}