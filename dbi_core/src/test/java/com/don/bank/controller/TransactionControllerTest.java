package com.don.bank.controller;

import com.don.bank.dto.TransactionDTO;
import com.don.bank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void getTransactions_ShouldReturnAllTransactions_WhenNoFilterIsProvided() throws Exception {

        List<TransactionDTO> transactions = List.of(
                TransactionDTO.builder().id(1L).amount(100.0).build(),
                TransactionDTO.builder().id(2L).amount(200.0).build()
        );
        when(transactionService.getAllTransactions()).thenReturn(transactions);

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].amount").value(200.0));
    }

    @Test
    void getTransactions_ShouldReturnFilteredTransactions_WhenFiltersAreProvided() throws Exception {

        long senderId = 101L, receiverId = 102L;
        List<TransactionDTO> transactions = List.of(
                TransactionDTO.builder().id(1L).amount(100.0).accountId(senderId).recipientId(receiverId).build()
        );
        when(transactionService.getTransactionsBySenderAndRecipientId(senderId, receiverId)).thenReturn(transactions);

        mockMvc.perform(get("/transactions")
                        .param("sid", String.valueOf(senderId))
                        .param("rid", String.valueOf(receiverId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].amount").value(100.0));
    }

    @Test
    void getTransaction_ShouldReturnTransaction_WhenTransactionExists() throws Exception {

        long transactionId = 1L;
        TransactionDTO transaction = TransactionDTO.builder().id(1L).amount(100.0).build();
        when(transactionService.getTransactionById(transactionId)).thenReturn(transaction);

        mockMvc.perform(get("/transactions/{id}", transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transactionId))
                .andExpect(jsonPath("$.amount").value(100.0));
    }

    @Test
    void getTransaction_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {

        long transactionId = 1L;
        when(transactionService.getTransactionById(transactionId)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/transactions/{id}", transactionId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error retrieving transaction by ID: Unexpected error"));
    }

    @Test
    void transfer_ShouldTransferMoney_WhenRequestIsValid() throws Exception {

        doNothing().when(transactionService).transferMoney(any(TransactionDTO.class));

        mockMvc.perform(post("/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderId\": 101, \"receiverId\": 102, \"amount\": 100.0}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Money transferred successfully"));
    }

    @Test
    void transfer_ShouldReturnBadRequest_WhenInvalidInputIsProvided() throws Exception {

        doThrow(new IllegalArgumentException("Invalid transaction data"))
                .when(transactionService).transferMoney(any(TransactionDTO.class));

        mockMvc.perform(post("/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderId\": 101, \"receiverId\": 102, \"amount\": -100.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error transferring money: Invalid transaction data"));
    }

    @Test
    void transfer_ShouldReturnInternalServerError_WhenUnexpectedErrorOccurs() throws Exception {

        doThrow(new RuntimeException("Unexpected error"))
                .when(transactionService).transferMoney(any(TransactionDTO.class));

        mockMvc.perform(post("/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderId\": 101, \"receiverId\": 102, \"amount\": 100.0}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error transferring money: Unexpected error"));
    }
}