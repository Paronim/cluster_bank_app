package com.donbank;
import com.donbank.service.CLIService;

/**
 * A console application that handles bank transactions.
 * This application allows users to perform various banking operations through a command-line interface.
 *
 * @author Vlad
 * @version 1.0
 */
public class DonBankApp {

    /**
     * The entry point of the application.
     *
     * @param args command-line arguments passed during the execution of the program.
     */
    public static void main(String[] args) {

        CLIService cliService = new CLIService();

        cliService.start();

    }
}