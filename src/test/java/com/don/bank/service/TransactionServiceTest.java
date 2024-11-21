package com.don.bank.service;

import com.don.bank.dto.AccountDTO;
import com.don.bank.dto.TransactionDTO;
import com.don.bank.entity.Account;
import com.don.bank.entity.Transaction;
import com.don.bank.repository.TransactionRepository;
import com.don.bank.util.mappingUtils.MappingUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ConvertorCurrencyService convertorCurrencyService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void transferMoney_shouldTransferAmountSuccessfully() {
        TransactionDTO transactionDTO = TransactionDTO.builder().amount(100d).accountId(1L).recipientId(2L).build();

        Account senderAccount = Account.builder().id(1L).balance(100d).currency(Account.Currency.RUB).build();

        Account recipientAccount = Account.builder().id(2L).balance(0d).currency(Account.Currency.USD).build();

        Transaction transaction = Transaction.builder().amount(100d).account(senderAccount).recipient(recipientAccount).build();

        when(accountService.withdrawBalance(1L, 100.0)).thenReturn(AccountDTO.builder().build());
        when(accountService.getAccountById(1L)).thenReturn(senderAccount);
        when(accountService.getAccountById(2L)).thenReturn(recipientAccount);
        when(convertorCurrencyService.convert(100.0, "RUB", "USD")).thenReturn(1.5); // Simulating currency conversion
        when(accountService.depositBalance(2L, 1.5d)).thenReturn(AccountDTO.builder().build());

        transactionService.transferMoney(transactionDTO);

        verify(accountService, times(1)).withdrawBalance(1L, 100d);
        verify(accountService, times(1)).depositBalance(2L, 1.5d);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void transferMoney_shouldThrowException_whenSenderAccountNotFound() {
        TransactionDTO transactionDTO = TransactionDTO.builder().amount(100d).accountId(1L).recipientId(2L).build();

        when(accountService.getAccountById(1L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> transactionService.transferMoney(transactionDTO));

        assertEquals("Account not found", exception.getMessage());
        verify(accountService, times(1)).getAccountById(1L);
        verify(transactionRepository, times(0)).save(any(Transaction.class)); // Transaction not saved
    }

    @Test
    void transferMoney_shouldThrowException_whenRecipientAccountNotFound() {
        TransactionDTO transactionDTO = TransactionDTO.builder().accountId(1L).recipientId(2L).build();
        transactionDTO.setAmount(100.0);

        Account senderAccount = new Account();
        senderAccount.setId(1L);
        senderAccount.setCurrency(Account.Currency.RUB);

        when(accountService.getAccountById(1L)).thenReturn(senderAccount);
        when(accountService.getAccountById(2L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> transactionService.transferMoney(transactionDTO));

        assertEquals("Account not found", exception.getMessage());
        verify(accountService, times(1)).getAccountById(1L);
        verify(accountService, times(1)).getAccountById(2L);
        verify(transactionRepository, times(0)).save(any(Transaction.class)); // Transaction not saved
    }
}