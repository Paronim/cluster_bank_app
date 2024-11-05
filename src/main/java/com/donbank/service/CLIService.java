package com.donbank.service;

import com.donbank.config.Config;
import com.donbank.config.LoggerConfig;
import com.donbank.db.DatabaseConnector;
import com.donbank.entity.Account;
import com.donbank.exception.AccountNotFoundException;
import lombok.SneakyThrows;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CLIService provides a command-line interface for interacting with clients and their accounts.
 * This service allows clients to perform various operations such as depositing funds, withdrawing funds,
 * creating accounts, deleting accounts, retrieving all transactions, and importing data from CSV files.
 */
public class CLIService {

    private ClientService clientService;
    private AccountService accountService;
    private TransactionService transactionService;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();
    private final Config config = Config.getInstance();
    private final Logger logger = LoggerConfig.getInstance().getLogger();

    /**
     * Initializes a new instance of CLIService.
     */
    public CLIService() {
    }

    private final Scanner inputConsole = new Scanner(System.in);

    /**
     * Starts the command-line interface, prompting the user for a client ID and allowing them
     * to execute various account-related commands in a loop until they choose to exit.
     *
     * @throws SQLException if database connection issues occur.
     */
    public void start() throws SQLException {
        System.out.println("Enter client ID");

        int id = getIntegerInput("Invalid input. Please enter a valid client ID.");

        accountService = new AccountService();
        List<Account> accountList = accountService.getAccountsByIdClient(id);
        clientService = new ClientService(id, accountList);
        transactionService = new TransactionService();

        System.out.println(clientService.getClient().toString());

        while (true) {
            System.out.println("Choose command\n");
            System.out.println("1. Top up account");
            System.out.println("2. Withdraw account");
            System.out.println("3. Create account");
            System.out.println("4. Delete account");
            System.out.println("5. get all transactions");
            System.out.println("6. Import CSV data");
            System.out.println("7. toggle log output");
            System.out.println("8. exit\n");

            int inputCommandNumber = getIntegerInput("Invalid input. Please enter a number corresponding to a command.");

            if (inputCommandNumber == 8) {
                DatabaseConnector.getInstance().closeConnection();
                break;
            }

            Consumer<Integer> commandFunc = getCommands().get(inputCommandNumber);

            if (commandFunc == null) {
                logger.log(Level.SEVERE, "Invalid command. Please try again.");

                continue;
            }

            commandFunc.accept(id);
        }

        inputConsole.close();
    }

    /**
     * Processes and validates integer input from the user.
     *
     * @param error the error message displayed for invalid input.
     * @return the integer value entered by the user.
     */
    private int getIntegerInput(String error) {
        while (true) {
            try {
                return inputConsole.nextInt();
            } catch (Exception e) {
                System.out.println(error);
                logger.log(Level.SEVERE, e.getMessage());
                inputConsole.next();
            }
        }
    }

    /**
     * Processes and validates double input from the user.
     *
     * @param error the error message displayed for invalid input.
     * @return the double value entered by the user.
     */
    private double getDoubleInput(String error) {
        while (true) {
            try {
                return inputConsole.nextDouble();
            } catch (Exception e) {
                System.out.println(error);
                logger.log(Level.SEVERE, e.getMessage());
                inputConsole.next();
            }
        }
    }

    /**
     * Prompts the user to select a currency from available options.
     * Displays a list of currencies with corresponding indices and expects
     * valid integer input from the user.
     *
     * @return the selected currency as a string.
     */
    private String getCurrency() {
        System.out.println("\nEnter a currency from the available ones");
        for (Account.Currency currency : Account.Currency.values()) {
            System.out.println(currency.ordinal() + 1 + ". " + currency);
        }

        while (true) {
            int numberCurrency = getIntegerInput("Invalid input. Please enter a number corresponding to a currency.");

            if (numberCurrency <= Account.Currency.values().length && numberCurrency > 0) {
                return Account.Currency.values()[numberCurrency - 1].toString();

            } else {
                logger.log(Level.SEVERE, "Not valid currency");
                System.out.println("Not valid currency");
            }
        }

    }

    /**
     * Modifies the balance of the client’s account based on the specified parameter
     * (e.g., deposit or withdrawal) within a database transaction.
     *
     * @param param parameter indicating the type of balance change ("withdraw" or "contribute").
     */
    private void changeBalanceAccount(String param) {
        String result;

        String currency = getCurrency();
        List<Account> accountsList = clientService.getClient().getAccounts();

        try {

            System.out.println(accountService.getAccountsByCurrency(currency, accountsList).toString());
            System.out.println("\nEnter amount");
            double amount = getDoubleInput("Invalid input. Please enter a float amount.");

            try {
                connection.setAutoCommit(false);
                result = accountService.depositFunds(currency, amount, accountsList, param) + "\n" + accountService.getAccountsByCurrency(currency, accountsList).toString();

                transactionService.addTransaction(amount, param, accountService.getAccountsByCurrency(currency, accountsList).getId());
                connection.commit();

                logger.log(Level.INFO, result);
                System.out.println(result);
            } catch (Exception e) {
                connection.rollback();
                logger.log(Level.SEVERE, e.getMessage());

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Creates a new account for the specified client based on user input.
     *
     * @param id the client’s ID.
     */
    private void createAccount(int id) {
        String result;

        String currency = getCurrency();

        try {
            result = accountService.createAccount(currency, clientService.getClient().getAccounts(), id);
            logger.log(Level.INFO, result);
            System.out.println(result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Removes an account for the specified client based on user input.
     * This operation is performed within a database transaction.
     *
     * @param id the client’s ID.
     */
    @SneakyThrows
    private void removeAccount(int id) {

        String currency = getCurrency();

        try {
            connection.setAutoCommit(false);
            accountService.deleteAccount(currency, clientService.getClient().getAccounts());
            clientService.getClient().setAccounts(accountService.getAccountsByIdClient(id));
            logger.log(Level.INFO, "Account deleted");
            System.out.println("Account deleted");
        } catch (SQLException | AccountNotFoundException e) {
            connection.rollback();
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Retrieves and displays all transactions for the client, filtered by the selected currency.
     */
    private void getTransactions() {
        String currency = getCurrency();

        try {
            String result = transactionService.getTransactions(currency, clientService.getClient().getAccounts()).toString();
            logger.log(Level.INFO, result);
            System.out.println(result);
        } catch (AccountNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Imports client and account data from CSV files.
     * This operation is performed within a database transaction.
     */
    @SneakyThrows
    private void importCSV() {

        System.out.println("Import start\n" +
                "path to CSV accounts: " + config.getProperty("csv.accounts") +
                "\npath to CSV clients: " + config.getProperty("csv.clients"));
        try {
            connection.setAutoCommit(false);
            List<Account> accounts = accountService.getListAccountsCSV(config.getProperty("csv.accounts"));
            clientService.importCSVClients(accounts, config.getProperty("csv.clients"));
            accountService.importCSVAccounts(accounts);
            logger.log(Level.INFO, "Import successfully.");
        } catch (SQLException | FileNotFoundException e) {
            connection.rollback();
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void switchLogOutput(){
        enum typeLogging {
            console,
            file
        }

        for(typeLogging logging: typeLogging.values()){
            if(!config.getProperty("logging.param").equals(String.valueOf(logging))){
                config.setProperty("logging.param", String.valueOf(logging));

                System.out.println("Log type switched to " + logging);
                break;
            }
        }
    }

    /**
     * Retrieves a map of command functions corresponding to user commands.
     * Keys are command numbers, values are functions that execute the respective commands.
     *
     * @return a map of commands.
     */
    private Map<Integer, Consumer<Integer>> getCommands() {
        Map<Integer, Consumer<Integer>> commands = new HashMap<>();
        commands.put(1, (id) -> changeBalanceAccount("contribute"));
        commands.put(2, (id) -> changeBalanceAccount("withdraw"));
        commands.put(3, this::createAccount);
        commands.put(4, this::removeAccount);
        commands.put(5, (id) -> getTransactions());
        commands.put(6, (id) -> importCSV());
        commands.put(7, (id) -> switchLogOutput());

        return commands;
    }
}
