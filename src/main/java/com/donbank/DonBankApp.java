package com.donbank;
import com.donbank.service.CLIService;

public class DonBankApp {

    public static void main(String[] args) {

        CLIService cliService = new CLIService();

        cliService.start();

    }
}