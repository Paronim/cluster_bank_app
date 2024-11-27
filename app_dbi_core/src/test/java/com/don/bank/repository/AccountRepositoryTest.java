package com.don.bank.repository;

import com.don.bank.entity.Account;
import com.don.bank.entity.Client;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureEmbeddedDatabase
@ActiveProfiles("test")
class AccountRepositoryTest {

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
    void findByClientId_shouldReturnAccountsForGivenClientId() {

        Client client = Client.builder().firstName("John").lastName("Doe").build();
        client = clientRepository.save(client);

        Account account1 = Account.builder()
                .currency(Account.Currency.RUB)
                .name("RUB")
                .balance(100d)
                .client(client)
                .type(Account.Type.main)
                .build();

        Account account2 = Account.builder()
                .currency(Account.Currency.RUB)
                .name("USD")
                .balance(200d)
                .client(client)
                .type(Account.Type.secondary)
                .build();

        accountRepository.save(account1);
        accountRepository.save(account2);

        List<Account> accounts = accountRepository.findByClientId(client.getId());

        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        assertTrue(accounts.stream().anyMatch(account -> account.getBalance() == 100.0));
        assertTrue(accounts.stream().anyMatch(account -> account.getBalance() == 200.0));
    }

}