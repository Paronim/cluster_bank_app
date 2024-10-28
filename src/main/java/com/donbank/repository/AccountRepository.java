package com.donbank.repository;

import com.donbank.entity.Account;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AccountRepository manages the storage and retrieval of account data.
 * It loads account information from a specified CSV file and provides methods to access account records.
 */
public class AccountRepository {

    private final List<Account> accountsData;
    private String FILE_PATH = "src/main/resources/accounts.csv";

    /**
     * Initializes a new instance of AccountRepository using the default file path for account data.
     */
    public AccountRepository() {
        this.accountsData = getListAccounts();
    }

    /**
     * Initializes a new instance of AccountRepository with a custom file path for account data.
     *
     * @param path the file path to the CSV file containing account data.
     */
    public AccountRepository(String path) {
        this.FILE_PATH = path;
        this.accountsData = getListAccounts();
    }

    /**
     * Retrieves a list of accounts associated with a specific client ID.
     *
     * @param idClient the unique identifier of the client whose accounts are to be retrieved.
     * @return a list of Account objects associated with the specified client ID.
     */
    public List<Account> getAccountsByIdClient(int idClient) {
        return accountsData.stream()
                .filter(a -> a.getClientId() == idClient)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all accounts data.
     *
     * @return a list of all Account objects in the repository.
     */
    public List<Account> getAccountsData() {
        return accountsData;
    }

    /**
     * Loads account data from the CSV file and creates Account objects.
     *
     * @return a list of Account objects loaded from the file.
     */
    private List<Account> getListAccounts() {
        List<Account> clientList = new ArrayList<>();
        try {
            File clients = new File(FILE_PATH);
            Scanner reader = new Scanner(clients);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");

                clientList.add(new Account(
                        Integer.parseInt(data[0]),
                        data[1],
                        Double.parseDouble(data[2]),
                        Integer.parseInt(data[3])
                ));
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
