package com.donbank.repository;

import com.donbank.entity.Account;
import com.donbank.entity.Client;

import java.io.*;
import java.util.*;

public class ClientRepository {

    private final Map<Integer, Client> clientsData;
    private String FILE_PATH = "src/main/resources/clients.csv";

    public ClientRepository(List<Account> accounts) {
        this.clientsData = getListClients(accounts);
    }

    public ClientRepository(List<Account> accounts, String path) {
        this.FILE_PATH = path;
        this.clientsData = getListClients(accounts);
    }

    public Client findById(int id){
        return clientsData.get(id);
    }

    private Map<Integer, Client> getListClients(List<Account> accounts){
        Map<Integer, Client> clientList = new HashMap<>();
        try
        {
            File clients = new File(FILE_PATH);
            Scanner reader = new Scanner(clients);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");

                int idClient = Integer.parseInt(data[0]);
                List<Account> filteredAccounts = accounts.stream().filter(a -> a.getClientId() == idClient).toList();

                clientList.put(idClient, new Client(idClient, data[1], data[2], filteredAccounts));
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
