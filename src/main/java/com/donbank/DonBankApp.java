package com.donbank;
import com.donbank.service.CLIService;

import java.sql.SQLException;

/**
 * A console application that handles bank transactions.
 * This application allows users to perform various banking operations through a command-line interface.
 *
 * <p>The application provides functionalities for managing clients, accounts, and transactions.
 * Users can perform operations such as creating accounts, making deposits, withdrawals, and viewing transaction history.</p>
 *
 * <p>Usage:</p>
 * <ul>
 *     <li>Start the application from the command line.</li>
 *     <li>Follow the prompts to enter commands and perform banking operations.</li>
 * </ul>
 *
 * @author Vlad
 * @version 1.0
 */
public class DonBankApp {

    /**
     * The entry point of the application.
     *
     * <p>This method initializes the command-line interface service and starts the application.</p>
     *
     * @param args command-line arguments passed during the execution of the program.
     *             These can be used to configure application behavior (if applicable).
     * @throws SQLException if there is an error connecting to the database or executing SQL queries.
     */
    public static void main(String[] args) throws SQLException {
        CLIService cliService = new CLIService();
        cliService.start();
    }
}
