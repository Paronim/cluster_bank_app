package com.don.bank.repository;

import com.don.bank.entity.Account;
import com.don.bank.entity.Client;
import com.don.bank.entity.Transaction;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureEmbeddedDatabase
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    Flyway flyway;

    @AfterEach
    public void cleanup() {
        flyway.clean();
    }

    @Test
    void findByAccountAndRecipient_shouldReturnTransactionsForGivenAccountAndRecipient() {

        Client client1 = Client.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        Client client2 = Client.builder()
                .firstName("Jane")
                .lastName("Smith")
                .build();
        client1 = clientRepository.save(client1);
        client2 = clientRepository.save(client2);

        Account account1 = Account.builder()
                .name("Account first client")
                .currency(Account.Currency.USD)
                .balance(1000.0)
                .client(client1)
                .type(Account.Type.main)
                .build();
        Account account2 = Account.builder()
                .name("Account second client")
                .currency(Account.Currency.USD)
                .balance(2000.0)
                .client(client2)
                .type(Account.Type.main)
                .build();
        account1 = accountRepository.save(account1);
        account2 = accountRepository.save(account2);

        Transaction transaction1 = Transaction.builder()
                .account(account1)
                .recipient(account2)
                .amount(100.0)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .transactionType(Transaction.TransactionType.TRANSFER)
                .build();
        Transaction transaction2 = Transaction.builder()
                .account(account1)
                .recipient(account2)
                .amount(200.0)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .transactionType(Transaction.TransactionType.TRANSFER)
                .build();
        Transaction transaction3 = Transaction.builder()
                .account(account2)
                .recipient(account1)
                .amount(150.0)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .transactionType(Transaction.TransactionType.TRANSFER)
                .build();
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);

        List<Transaction> transactions = transactionRepository.findByAccountAndRecipient(account1.getId(), account2.getId());

        assertThat(transactions).isNotNull();
        assertThat(transactions).hasSize(2);
        Account finalAccount = account1;
        assertThat(transactions).allMatch(tx -> tx.getAccount().equals(finalAccount));
        Account finalAccount1 = account2;
        assertThat(transactions).allMatch(tx -> tx.getRecipient().equals(finalAccount1));
        assertThat(transactions).extracting(Transaction::getAmount).containsExactlyInAnyOrder(100.0, 200.0);
    }

}