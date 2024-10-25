package com.donbank.service;

import com.donbank.entity.Account;

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

    public void start(){
        Scanner inputConsole = new Scanner(System.in);

        System.out.println("Enter client ID");
        int id = inputConsole.nextInt();

        accountService = new AccountService();
        List<Account> accountList = accountService.getAccountsByIdClient(id);
        clientService = new ClientService(accountList);

        while (true) {
            System.out.println("Choose command\n");
            System.out.println("1. Top up account");
            System.out.println("2. Withdraw account");
            System.out.println("3. Delete account");
            System.out.println("4. Create account");
            System.out.println("5. exit\n");

            int inputCommandNumber = inputConsole.nextInt();

            if(inputCommandNumber == 5){break;}

            Function<Integer, String> commandFunc = getCommands().get(inputCommandNumber);

            if(commandFunc == null){
                System.out.println("Invalid command\n");
                continue;
            }

            System.out.println(commandFunc.apply(id));
        }

        inputConsole.close();
    }

    private Map<Integer, Function<Integer, String>> getCommands() {
        Map<Integer, Function<Integer, String>> commands = new HashMap<>();
//        commands.put(1, (id) -> {
//            System.out.println("1. Enter summ");
//            clientService.getClientById(id);
//        });
//        commands.put(2, (id) -> );
//        commands.put(3, (id) -> );
//        commands.put(4, (id) -> );

        return commands;
    }
}
