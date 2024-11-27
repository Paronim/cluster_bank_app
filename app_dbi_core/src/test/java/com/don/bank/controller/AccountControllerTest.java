package com.don.bank.controller;

import com.don.bank.dto.AccountDTO;
import com.don.bank.dto.ClientDTO;
import com.don.bank.entity.Account;
import com.don.bank.service.AccountService;
import com.don.bank.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ClientService clientService;

    @Test
    void getAccounts_ShouldReturnAccounts_WhenClientIdIsNull() throws Exception {

        List<AccountDTO> accounts = Arrays.asList(AccountDTO.builder().id(1L).name("Account 1").build(), AccountDTO.builder().id(2L).name("Account 2").build());
        when(accountService.getAllAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getAccounts_ShouldReturnFilteredAccounts_WhenClientIdIsProvided() throws Exception {

        Long clientId = 1L;
        List<AccountDTO> accounts = Arrays.asList(AccountDTO.builder().id(1L).name("Account 1").build());
        when(accountService.getAccountsByClientId(clientId)).thenReturn(accounts);

        mockMvc.perform(get("/accounts")
                        .param("cid", String.valueOf(clientId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void createAccount_ShouldReturnOk_WhenAccountIsCreated() throws Exception {

        AccountDTO accountDTO = AccountDTO.builder().name("Account 1").clientId(1L).build();
        when(clientService.getById(accountDTO.getClientId())).thenReturn(ClientDTO.builder().id(1L).firstName("Client").lastName("1").build());
        when(accountService.createAccount(accountDTO)).thenReturn(accountDTO);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clientId\": 1, \"name\": \"Account 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Account 1"))
                .andExpect(jsonPath("$.clientId").value(1L));
    }

    @Test
    void createAccount_ShouldReturnBadRequest_WhenClientIdIsInvalid() throws Exception {

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clientId\": 0, \"name\": \"Invalid Account\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Client id is required"));
    }

    @Test
    void updateAccount_ShouldReturnOk_WhenAccountIsUpdated() throws Exception {

        AccountDTO accountDTO = AccountDTO.builder().id(1L).name("Updated Account").build();
        when(accountService.getAccountById(accountDTO.getId())).thenReturn(Account.builder().id(1L).name("Account").build());
        when(accountService.updateAccount(accountDTO)).thenReturn(accountDTO);

        mockMvc.perform(put("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Updated Account\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Account"));
    }
    @Test
    void updateAccount_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {

        AccountDTO accountDTO = AccountDTO.builder().id(99L).name("Nonexistent Account").build();
        when(accountService.getAccountById(accountDTO.getId())).thenReturn(null);

        mockMvc.perform(put("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 99, \"name\": \"Nonexistent Account\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Error updating account: Account not found"));
    }

    @Test
    void deleteAccount_ShouldReturnOk_WhenAccountIsDeleted() throws Exception {

        long accountId = 1L;
        doNothing().when(accountService).deleteAccount(accountId);

        mockMvc.perform(delete("/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deleted " + accountId));
    }

    @Test
    void withdraw_ShouldReturnOk_WhenWithdrawalIsSuccessful() throws Exception {

        long accountId = 1L;
        double amount = 100.0;
        when(accountService.withdrawBalance(accountId, amount)).thenReturn(AccountDTO.builder().id(accountId).balance(1000.0).build());

        mockMvc.perform(post("/accounts/{id}/withdraw", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("1000.0"));
    }

    @Test
    void deposit_ShouldReturnOk_WhenDepositlIsSuccessful() throws Exception {

        long accountId = 1L;
        double amount = 100.0;
        when(accountService.depositBalance(accountId, amount)).thenReturn(AccountDTO.builder().id(accountId).balance(1000.0).build());

        mockMvc.perform(post("/accounts/{id}/deposit", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("1000.0"));
    }
}