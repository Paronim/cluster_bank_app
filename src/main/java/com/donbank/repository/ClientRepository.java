package com.donbank.repository;

import com.donbank.entity.Account;
import com.donbank.entity.Client;

import java.io.*;
import java.util.*;


/**
 * ClientRepository manages the storage and retrieval of client data.
 * It loads client information from a specified CSV file and provides methods to access client records.
 */
public class ClientRepository {

    private final Map<Integer, Client> clientsData;
    private String FILE_PATH = "src/main/resources/clients.csv";

    /**
     * Initializes a new instance of ClientRepository with the specified list of accounts.
     * The repository will use the default file path for client data.
     *
     * @param accounts the list of accounts associated with the clients.
     */
    public ClientRepository(List<Account> accounts) {
        this.clientsData = getListClients(accounts);
    }

    /**
     * Initializes a new instance of ClientRepository with the specified list of accounts
     * and a custom file path for client data.
     *
     * @param accounts the list of accounts associated with the clients.
     * @param path the file path to the CSV file containing client data.
     */
    public ClientRepository(List<Account> accounts, String path) {
        this.FILE_PATH = path;
        this.clientsData = getListClients(accounts);
    }

    /**
     * Finds a client by their unique identifier.
     *
     * @param id the unique identifier of the client to retrieve.
     * @return the Client object associated with the specified id, or null if not found.
     */
    public Client findById(int id) {
        return clientsData.get(id);
    }

    /**
     * Loads client data from the CSV file and associates it with the provided accounts.
     *
     * @param accounts the list of accounts used to filter clients.
     * @return a map where the key is the client ID and the value is the corresponding Client object.
     */
    private Map<Integer, Client> getListClients(List<Account> accounts) {
        Map<Integer, Client> clientList = new HashMap<>();
        try {
            File clients = new File(FILE_PATH);
            Scanner reader = new Scanner(clients);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");

                int idClient = Integer.parseInt(data[0]);
                List<Account> filteredAccounts = accounts.stream()
                        .filter(a -> a.getClientId() == idClient)
                        .toList();

                clientList.put(idClient, new Client(idClient, data[1], data[2], filteredAccounts));
            }
            reader.close();

            return clientList;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return clientList;
    }
}
