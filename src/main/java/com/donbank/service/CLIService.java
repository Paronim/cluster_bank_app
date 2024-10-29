package com.donbank.service;

import com.donbank.entity.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

/**
 * CLIService provides a command-line interface for interacting with clients and their accounts.
 * It allows clients to perform various operations such as depositing funds, withdrawing funds,
 * creating accounts and deleting accounts.
 */
public class CLIService {

    private ClientService clientService;
    private AccountService accountService;

    /**
     * Initializes a new instance of CLIService.
     */
    public CLIService() {
    }

    private final Scanner inputConsole = new Scanner(System.in);

    /**
     * Starts the command-line interface, prompting the user for a client ID and allowing them
     * to perform various account-related commands in a loop until they choose to exit.
     */
    public void start() {
        System.out.println("Enter client ID");

        int id = getIntegerInput("Invalid input. Please enter a valid client ID.");

        accountService = new AccountService();
        List<Account> accountList = accountService.getAccountsByIdClient(id);
        clientService = new ClientService(accountList);

        while (true) {
            System.out.println("Choose command\n");
            System.out.println("1. Top up account");
            System.out.println("2. Withdraw account");
            System.out.println("3. Create account");
            System.out.println("4. Delete account");
            System.out.println("6. exit\n");

            int inputCommandNumber = getIntegerInput("Invalid input. Please enter a number corresponding to a command.");

            if (inputCommandNumber == 6) {
                break;
            }

            Function<Integer, String> commandFunc = getCommands().get(inputCommandNumber);

            if (commandFunc == null) {
                System.out.println("Invalid command. Please try again.\n");
                continue;
            }

            System.out.println(commandFunc.apply(id));
        }

        inputConsole.close();
    }

    /**
     * Retrieves the number value from user input, handling invalid input until a valid integer is provided.
     *
     * @param error the error message.
     * @return user-entered number of the selected parameter.
     */
    private int getIntegerInput(String error) {
        while (true) {
            try {
                return inputConsole.nextInt();
            } catch (Exception e) {
                System.out.println(error);
                inputConsole.next();
            }
        }
    }

    /**
     * Prompts the user to select a currency from the available options.
     * Displays each currency with its corresponding index and expects
     * a valid integer input from the user. If an invalid input is provided,
     * the user will be prompted to try again.
     *
     * @return The selected currency as a String.
     */
    private String getCurrency() {
        System.out.println("\nEnter a currency from the available ones");
        for (Account.Currency currency : Account.Currency.values()) {
            System.out.println(currency.ordinal() + 1 + ". " + currency);
        }

        while (true) {
            int numberCurrency = getIntegerInput("Invalid input. Please enter a number corresponding to a currency.");

            if (numberCurrency <= Account.Currency.values().length && numberCurrency > 0) {
                return Account.Currency.values()[numberCurrency-1].toString();

            } else {
                System.out.println("Not valid currency");
            }
        }

    }

    /**
     * Changes the balance of the client's account based on the specified parameter (withdraw or contribute).
     *
     * @param id    the ID of the client.
     * @param param the parameter indicating the type of balance change ("withdraw" or "contribute").
     * @return a message indicating the result of the operation.
     */
    private String changeBalanceAccount(int id, String param) {
        String result;

        String currency = getCurrency();
        List<Account> accountsList = clientService.getClientById(id).getAccounts();

        try {
            System.out.println(accountService.getAccountsByCurrency(currency, accountsList).toString());
            System.out.println("\nEnter amount");

            int amount = inputConsole.nextInt();
            result = accountService.depositFunds(currency, amount, accountsList, param)  + "\n" + accountService.getAccountsByCurrency(currency, accountsList).toString();

        } catch (Exception e) {
            result = "Error while processing the deposit: " + e.getMessage();
        }
        return result;
    }

    /**
     * Creates a new account for the specified client based on user input.
     *
     * @param id the ID of the client.
     * @return a message indicating the result of the account creation operation.
     */
    private String createAccount(int id) {
        String result;

        String currency = getCurrency();

        try {
            result = accountService.createAccount(currency, clientService.getClientById(id).getAccounts(), id);
        } catch (Exception e) {
            result = "Error while processing the create account: " + e.getMessage();
        }
        return result;
    }

    /**
     * Removes an account for the specified client based on user input.
     *
     * @param id the ID of the client.
     * @return a message indicating the result of the account removal operation.
     */
    private String removeAccount(int id) {
        String result;

        String currency = getCurrency();

        try {
            result = accountService.deleteAccount(currency, clientService.getClientById(id).getAccounts());
        } catch (Exception e) {
            result = "Error while processing the remove account: " + e.getMessage();
        }
        return result;
    }

    /**
     * Retrieves a map of command functions, each corresponding to a specific user command.
     *
     * @return a map where the key is the command number and the value is a function that executes the command.
     */
    private Map<Integer, Function<Integer, String>> getCommands() {
        Map<Integer, Function<Integer, String>> commands = new HashMap<>();
        commands.put(1, (id) -> changeBalanceAccount(id, "contribute"));
        commands.put(2, (id) -> changeBalanceAccount(id, "withdraw"));
        commands.put(3, (id) -> createAccount(id));
        commands.put(4, (id) -> removeAccount(id));

        return commands;
    }
}
