package com.donbank;
import com.donbank.entity.Account;
import com.donbank.service.AccountService;
import com.donbank.service.CLIService;
import com.donbank.service.ClientService;

import java.util.List;

/**
 * @author Vlad
 * @version 1.0
 * console application from bank transactions
 */
public class DonBankApp {

    public static void main(String[] args) {

        CLIService cliService = new CLIService();

        cliService.start();

    }
}