package com.donbank.repository;

import com.donbank.entity.Account;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AccountRepository {
    private final List<Account> accountsData;
    private String FILE_PATH = "src/main/resources/accounts.csv";

    public AccountRepository() {
        this.accountsData = getListAccounts();
    }

    public AccountRepository(String path) {
        this.FILE_PATH = path;
        this.accountsData = getListAccounts();
    }

    public List<Account> getAccountsByIdClient(int idClient){
        return accountsData.stream().filter(a -> a.getClientId() == idClient).collect(Collectors.toList());
    }

    public List<Account> getAccountsData(){
        return accountsData;
    }

    private List<Account> getListAccounts(){
        List<Account> clientList = new ArrayList<>();
        try
        {
            File clients = new File(FILE_PATH);
            Scanner reader = new Scanner(clients);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");

                clientList.add(new Account(Integer.parseInt(data[0]), data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3])));
            }
            reader.close();

            return clientList;
        } catch(IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return clientList;
    }


}
