package com.don.bank.util.mappingUtils;

import com.don.bank.dto.AccountDTO;
import com.don.bank.dto.ClientDTO;
import com.don.bank.dto.RegisterClientDTO;
import com.don.bank.dto.TransactionDTO;
import com.don.bank.entity.Account;
import com.don.bank.entity.Client;
import com.don.bank.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MappingUtils {

    public static ClientDTO mapToClientDto (Client client) {

        List<Account> accounts = client.getAccounts();

        List<Long> accountsId = null;

        if(accounts != null){
            accountsId = accounts.stream().map(Account::getId).toList();
        }

        return ClientDTO.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .phone(String.valueOf(client.getPhone()))
                .accounts(accountsId)
                .build();

    }

    public static Client mapToClient (ClientDTO clientDTO) {

        return Client.builder()
                .id(clientDTO.getId())
                .firstName(clientDTO.getFirstName())
                .lastName(clientDTO.getLastName())
                .phone(Long.parseLong(clientDTO.getPhone()))
                .build();
    }

    public static Client mapToClient (RegisterClientDTO clientDTO) {

        return Client.builder()
                .firstName(clientDTO.getFirstName())
                .lastName(clientDTO.getLastName())
                .phone(Long.parseLong(clientDTO.getPhone()))
                .password(clientDTO.getPassword())
                .build();
    }

    public static AccountDTO mapToAccountDto (Account account) {

        long clientId = account.getClient().getId();

        return AccountDTO.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .currency(String.valueOf(account.getCurrency()))
                .type(String.valueOf(account.getType()))
                .name(account.getName())
                .clientId(clientId)
                .build();
    }

    public static Account mapToAccount (AccountDTO accountDTO) {

        return Account.builder()
                .id(accountDTO.getId())
                .balance(accountDTO.getBalance())
                .currency(Account.Currency.valueOf(accountDTO.getCurrency()))
                .type(Account.Type.valueOf(accountDTO.getType()))
                .name(accountDTO.getName())
                .client(Client.builder().id(accountDTO.getClientId()).build())
                .build();
    }

    public static TransactionDTO mapToTransactionDto (Transaction transaction) {

        Account account = transaction.getRecipient();

        return TransactionDTO.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .createdAt(transaction.getCreatedAt())
                .transactionType(String.valueOf(transaction.getTransactionType()))
                .accountId(transaction.getAccount().getId())
                .recipientId(account != null ? account.getId() : null)
                .build();
    }

    public static Transaction mapToTransaction (TransactionDTO transactionDTO){

        return Transaction.builder()
                .id(transactionDTO.getId())
                .amount(transactionDTO.getAmount())
                .createdAt(transactionDTO.getCreatedAt())
                .transactionType(Transaction.TransactionType.valueOf(transactionDTO.getTransactionType()))
                .account(Account.builder().id(transactionDTO.getAccountId()).build())
                .recipient(transactionDTO.getRecipientId() != null ? Account.builder().id(transactionDTO.getRecipientId()).build() : null)
                .build();

    }
}
