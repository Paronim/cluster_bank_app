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
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CLIService provides a command-line interface for interacting with clients and their accounts.
 * This service allows clients to perform various operations, including managing user accounts,
 * performing transactions, and importing data from CSV files.
 */
public class CLIService {

    private ClientService clientService;
    private AccountService accountService;
    private TransactionService transactionService;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();
    private final Config config = Config.getInstance();
    private final Logger logger = LoggerConfig.getInstance().getLogger();
    private int id = 0;

    /**
     * Initializes a new instance of CLIService.
     */
    public CLIService() {
    }

    private final Scanner inputConsole = new Scanner(System.in);

    /**
     * Starts the CLI, prompting the user for a client ID and allowing them to execute
     * various account-related commands in a loop until they choose to exit.
     */
    public void start() {
        while (true) {
            clientService = new ClientService();
            accountService = new AccountService();

            System.out.println("Choose command\n");
            System.out.println("1. Choose user");
            System.out.println("2. Create user");
            System.out.println("3. Delete user");
            System.out.println("4. Exit\n");

            int inputCommandNumberFirst = getIntegerInput("Invalid input. Please enter a number corresponding to a command.");
            Runnable commandFuncFirst = getCommandsFirstLevel().get(inputCommandNumberFirst);

            if (commandFuncFirst == null) {
                logger.log(Level.SEVERE, "Invalid command. Please try again.");

                continue;
            }

            commandFuncFirst.run();

            if(id == 0){
                continue;
            }

            try {
                List<Account> accountList = accountService.getAccountsByIdClient(id);
                clientService = new ClientService(id, accountList);
                transactionService = new TransactionService();

                System.out.println(clientService.getClient().toString());
            } catch (Exception e) {
                System.out.println("Not found user");
                continue;
            }

            while (true) {
                System.out.println("Choose command\n");
                System.out.println("1. Top up account");
                System.out.println("2. Withdraw account");
                System.out.println("3. Create account");
                System.out.println("4. Delete account");
                System.out.println("5. get all transactions");
                System.out.println("6. Import CSV data");
                System.out.println("7. toggle log output");
                System.out.println("8. Exit\n");


                int inputCommandNumber = getIntegerInput("Invalid input. Please enter a number corresponding to a command.");

                if (inputCommandNumber == 8) {
                    id = 0;
                    break;
                }

                Consumer<Integer> commandFunc = getCommandsSecondLevel().get(inputCommandNumber);

                if (commandFunc == null) {
                    logger.log(Level.SEVERE, "Invalid command. Please try again.");

                    continue;
                }

                commandFunc.accept(id);
            }

        }
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
     * Processes and validates string input from the user.
     *
     * @param error Error message displayed for invalid input.
     * @return The string value entered by the user.
     */
    private String getStringInput(String error) {
        while (true) {
            try {
                return inputConsole.next();
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
     * Prompts the user to select an account based on currency.
     *
     * @return The selected account or null if user exits the selection.
     */
    private Account getCurrency() {
        System.out.println("\nEnter a currency from the available ones");

        List<Account> accounts = clientService.getClient().getAccounts().stream().sorted(Comparator.comparing(Account::getCurrency)).toList();

        for (int i = 1; i <= accounts.size(); i++) {
            Account account = accounts.get(i-1);
            System.out.println(i + ". Currency: " + account.getCurrency() + "; Name: " + account.getName());
        }
        System.out.println(accounts.size() + 1 + ". Exit");

        while (true) {
            int numberCurrency = getIntegerInput("Invalid input. Please enter a number corresponding to a currency.");

            if (numberCurrency <= accounts.size() && numberCurrency > 0) {
                return accounts.get(numberCurrency - 1);

            } else if (numberCurrency == accounts.size() + 1) {
                return null;
            } else {
                logger.log(Level.SEVERE, "Not valid currency");
                System.out.println("Not valid currency");
            }
        }

    }

    /**
     * Displays available currencies and prompts the user to select one.
     *
     * @return The selected currency as a string or null if user exits.
     */
    private String getAllCurrency() {
        System.out.println("\nEnter a currency from the available ones");
        for (Account.Currency currency : Account.Currency.values()) {
            System.out.println(currency.ordinal() + 1 + ". " + currency);
        }
        System.out.println(Account.Currency.values().length + 1 + ". Exit");

        while (true) {
            int numberCurrency = getIntegerInput("Invalid input. Please enter a number corresponding to a currency.");

            if (numberCurrency <= Account.Currency.values().length && numberCurrency > 0) {
                return Account.Currency.values()[numberCurrency - 1].toString();

            } else if (numberCurrency == Account.Currency.values().length + 1) {
                return null;
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

        Account account = getCurrency();

        if (account == null) return;

        try {

            System.out.println(account.toString());
            System.out.println("\nEnter amount");
            double amount = getDoubleInput("Invalid input. Please enter a float amount.");

            try {
                connection.setAutoCommit(false);
                result = accountService.depositFunds(account, amount, param) + "\n" + account.toString();

                transactionService.addTransaction(amount, param, account.getId());
                connection.commit();

                logger.log(Level.INFO, result);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
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

        String currency = getAllCurrency();

        if (currency == null) return;

        try {
            result = accountService.createAccount(currency, clientService.getClient().getAccounts(), id);
            logger.log(Level.INFO, result);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

        Account account = getCurrency();

        if (account == null) return;

        try {
            connection.setAutoCommit(false);
            accountService.deleteAccount(account);
            clientService.getClient().setAccounts(accountService.getAccountsByIdClient(id));
            logger.log(Level.INFO, "Account deleted");
            System.out.println("Account deleted");
        } catch (SQLException | AccountNotFoundException e) {
            connection.rollback();
            System.out.println(e.getMessage());
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Retrieves and displays all transactions for the client, filtered by the selected currency.
     */
    private void getTransactions() {
        Account account = getCurrency();

        if (account == null) return;

        try {
            String result = transactionService.getTransactions(account).toString();
            logger.log(Level.INFO, result);
            System.out.println(result);
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Imports client and account data from CSV files.
     * This operation is performed within a database transaction.
     */
    @SneakyThrows
    private void importCSV() {

        System.out.println("Import start.\n" +
                "path to CSV accounts: " + config.getProperty("csv.accounts") +
                "\npath to CSV clients: " + config.getProperty("csv.clients"));
        try {
            connection.setAutoCommit(false);
            List<Account> accounts = accountService.getListAccountsCSV(config.getProperty("csv.accounts"));
            clientService.importCSVClients(accounts, config.getProperty("csv.clients"));
            accountService.importCSVAccounts(accounts);
            logger.log(Level.INFO, "Import successfully.");
            System.out.println("Import successfully.");
        } catch (SQLException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            connection.rollback();
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Toggles between console and file log output.
     */
    private void switchLogOutput() {
        enum typeLogging {
            console,
            file
        }

        for (typeLogging logging : typeLogging.values()) {
            if (!config.getProperty("logging.param").equals(String.valueOf(logging))) {
                config.setProperty("logging.param", String.valueOf(logging));

                System.out.println("Log type switched to " + logging);
                break;
            }
        }
    }

    /**
     * Prompts the user to select a client by ID.
     */
    private void chooseUser(){
        System.out.println("Enter client ID");

        id = getIntegerInput("Invalid input. Please enter a valid client ID.");
    }

    /**
     * Creates a new user with the specified first and last name.
     */
    @SneakyThrows
    private void createNewUser(){
        System.out.println("Enter first name:");

        String firstName = getStringInput("Not valid first name");

        System.out.println("Enter last name:");

        String lastName = getStringInput("Not valid last name");

        try {
            connection.setAutoCommit(false);
            List<Account> accounts = new ArrayList<>();
            long id = clientService.addClient(firstName, lastName);
            accountService.createAccount("RUB", accounts, (int) id);
            accountService.createAccount("USD", accounts, (int) id);

            logger.log(Level.INFO, "Create client with id " + id + " successfully.");
            System.out.println("Create client successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            connection.rollback();
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Deletes an existing user by ID.
     */
    private void deleteUser(){
        System.out.println("Enter client ID");

        id = getIntegerInput("Invalid input. Please enter a valid client ID.");

        try {
            clientService.deleteClient(id);
            logger.log(Level.INFO, "Delete client successfully.");
            System.out.println("Delete client successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.log(Level.SEVERE, e.getMessage());
        }

        id = 0;
    }
    /**
     * Retrieves a map of command functions corresponding to user commands.
     * Keys are command numbers, values are functions that execute the respective commands.
     *
     * @return a map of commands.
     */
    private Map<Integer, Consumer<Integer>> getCommandsSecondLevel() {
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

    /**
     * Retrieves a map of secondary command functions for account interaction.
     *
     * @return A map of commands with keys as command numbers and values as functions to execute.
     */
    private Map<Integer, Runnable> getCommandsFirstLevel() {
        Map<Integer, Runnable> commands = new HashMap<>();
        commands.put(1, this::chooseUser);
        commands.put(2, this::createNewUser);
        commands.put(3, this::deleteUser);

        return commands;
    }
}
