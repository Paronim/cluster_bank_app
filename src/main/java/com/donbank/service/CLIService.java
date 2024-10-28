package com.donbank.service;

import com.donbank.entity.Account;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public class CLIService {

    private ClientService clientService;
    private AccountService accountService;

    public CLIService() {
    }

    private final Scanner inputConsole = new Scanner(System.in);

    public void start() {
        System.out.println("Enter client ID");

        int id = getClientId();

        accountService = new AccountService();
        List<Account> accountList = accountService.getAccountsByIdClient(id);
        clientService = new ClientService(accountList);

        while (true) {
            System.out.println("Choose command\n");
            System.out.println("1. Top up account");
            System.out.println("2. Withdraw account");
            System.out.println("3. Create account");

            // FIX ERROR ImmutableCollections
            System.out.println("4. Delete account");
            System.out.println("5. Serialization");
            System.out.println("6. exit\n");

            int inputCommandNumber = getCommandInput();

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

    private int getClientId() {
        while (true) {
            try {
                return inputConsole.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid client ID.");
                inputConsole.next();
            }
        }
    }

    private int getCommandInput() {
        while (true) {
            try {
                return inputConsole.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number corresponding to a command.");
                inputConsole.next();
            }
        }
    }

    private String changeBalanceAccount(int id, String param) {
        String result;
        System.out.println("\nEnter a currency from the available ones");
        for (AccountService.Currency currency : AccountService.Currency.values()) {
            System.out.println(currency);
        }

        String currency = inputConsole.next().toUpperCase();
        System.out.println("\nEnter amount");

        try {
            int amount = inputConsole.nextInt();
            result = accountService.depositFunds(currency, amount, clientService.getClientById(id).getAccounts(), param);
        } catch (Exception e) {
            result = "Error while processing the deposit: " + e.getMessage();
        }
        return result;
    }

    private String createAccount(int id) {
        String result;
        System.out.println("\nEnter a currency from the available ones");
        for (AccountService.Currency currency : AccountService.Currency.values()) {
            System.out.println(currency);
        }

        String currency = inputConsole.next().toUpperCase();

        try {
            result = accountService.createAccount(currency, clientService.getClientById(id).getAccounts(), id);
        } catch (Exception e) {
            result = "Error while processing the create account: " + e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    private String removeAccount(int id) {
        String result;
        System.out.println("\nEnter a currency from the available ones");
        for (AccountService.Currency currency : AccountService.Currency.values()) {
            System.out.println(currency);
        }

        String currency = inputConsole.next().toUpperCase();

        try {
            result = accountService.deleteAccount(currency, clientService.getClientById(id).getAccounts());
        } catch (Exception e) {
            result = "Error while processing the remove account: " + e.getMessage();
            e.printStackTrace();

        }
        return result;
    }

    private Map<Integer, Function<Integer, String>> getCommands() {
        Map<Integer, Function<Integer, String>> commands = new HashMap<>();
        commands.put(1, (id) -> changeBalanceAccount(id, "withdraw"));
        commands.put(2, (id) -> changeBalanceAccount(id, "contribute"));
        commands.put(3, (id) -> createAccount(id));
        commands.put(4, (id) -> removeAccount(id));
        commands.put(5, (id) -> {
            String result;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/person.json"))) {
                writer.write(clientService.getClientById(id).toString());
                result = "Serialized data is saved in person.json";
            } catch (IOException e) {
                result = "Error saving data: " + e.getMessage();
                e.printStackTrace();
            }
            return result;
        });

        return commands;
    }
}
