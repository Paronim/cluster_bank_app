package com.don.bank.service;

import com.don.bank.dto.AccountDTO;
import com.don.bank.entity.Account;
import com.don.bank.entity.Client;
import com.don.bank.entity.Transaction;
import com.don.bank.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void testGetAllAccounts() {

        List<Account> accounts = List.of(
                Account.builder().id(1L).name("Account 1").currency(Account.Currency.USD).client(Client.builder().id(1L).build()).build(),
                Account.builder().id(2L).name("Account 2").currency(Account.Currency.RUB).client(Client.builder().id(1L).build()).build()
        );
        when(accountRepository.findAll()).thenReturn(accounts);

        List<AccountDTO> accountsDTO = accountService.getAllAccounts();

        assertEquals(2, accountsDTO.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void testGetAccountsByClientId() {

        AccountDTO accountDTO = AccountDTO.builder().clientId(1L).currency("USD").name("New Account").build();

        Account savedAccount = Account.builder()
                .id(1L)
                .client(Client.builder().id(1L).build())
                .currency(Account.Currency.USD)
                .name("New Account")
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        AccountDTO result = accountService.createAccount(accountDTO);

        assertNotNull(result);
        assertEquals("USD", result.getCurrency());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateAccount() {

        Account oldAccount = Account.builder()
                .id(1L)
                .currency(Account.Currency.USD)
                .balance(100.0)
                .name("Old Account")
                .type(Account.Type.secondary)
                .client(Client.builder().id(1L).build())
                .build();

        AccountDTO accountDTO = AccountDTO.builder().id(1L).currency("USD").name("Updated Account").type("secondary").build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(oldAccount));
        oldAccount.setName("Updated Account");
        when(accountRepository.save(any(Account.class))).thenReturn(oldAccount);

        AccountDTO result = accountService.updateAccount(accountDTO);

        assertEquals("Updated Account", result.getName());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testDeleteAccount_MainAccountThrowsException() {

        Account account = Account.builder()
                .id(1L)
                .type(Account.Type.main)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deleteAccount(1L);
        });

        assertEquals("can't delete main account", exception.getMessage());
    }

    @Test
    void testWithdrawBalance_InsufficientFundsThrowsException() {

        Account account = Account.builder()
                .id(1L)
                .balance(50.0)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.withdrawBalance(1L, 100.0, "transaction");
        });

        assertEquals("not enough money", exception.getMessage());
    }

    @Test
    void testDepositBalance() {

        Account account = Account.builder()
                .id(1L)
                .balance(100.0)
                .client(Client.builder().id(1L).build())
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDTO result = accountService.depositBalance(1L, 50.0, "transaction");

        assertEquals(150.0, result.getBalance());
        verify(transactionService, times(1)).createTransaction(any(Transaction.class));
        verify(accountRepository, times(1)).save(account);
    }
}