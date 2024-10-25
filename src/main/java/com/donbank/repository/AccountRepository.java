package com.donbank.repository;

import com.donbank.entity.Account;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AccountRepository {
    private List<Account> accountsData;

    public AccountRepository() {
        this.accountsData = getListAccounts();
    }

    private static final String FILE_PATH = "src/main/resource/data/accounts.csv";

    public List<Account> getAccountsByIdClient(int idClient){
        return accountsData.stream().filter(a -> a.getClientId() == idClient).toList();
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
