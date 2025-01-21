package com.don.bank.controller;

import com.don.common.models.ClientDTO;
import com.don.bank.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Test
    void getClientById_ShouldReturnClient_WhenClientExists() throws Exception {

        long clientId = 1L;
        ClientDTO clientDTO = ClientDTO.builder().id(clientId).firstName("Client").lastName("Name").build();
        when(clientService.getById(clientId)).thenReturn(clientDTO);

        mockMvc.perform(get("/clients/{id}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientId))
                .andExpect(jsonPath("$.firstName").value("Client"));
    }

    @Test
    void getClientById_ShouldReturnNotFound_WhenClientDoesNotExist() throws Exception {

        long clientId = 1L;
        when(clientService.getById(clientId)).thenThrow(new EntityNotFoundException("Client not found"));

        mockMvc.perform(get("/clients/{id}", clientId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Client not found"));
    }

    @Test
    void getClientAccounts_ShouldReturnClientAccounts_WhenClientExists() throws Exception {

        long clientId = 1L;
        ClientDTO clientDTO = ClientDTO.builder().id(clientId).firstName("Client").lastName("Name").accounts(Arrays.asList(1L, 2L)).build();
        when(clientService.getById(clientId)).thenReturn(clientDTO);

        mockMvc.perform(get("/clients/{id}/accounts", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value(1L))
                .andExpect(jsonPath("$[1]").value(2L));
    }

    @Test
    void getClientAccounts_ShouldReturnNotFound_WhenClientDoesNotExist() throws Exception {

        long clientId = 1L;
        when(clientService.getById(clientId)).thenThrow(new EntityNotFoundException("Client not found"));

        mockMvc.perform(get("/clients/{id}/accounts", clientId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Client not found"));
    }

    @Test
    void updateClient_ShouldReturnUpdatedClient_WhenClientExists() throws Exception {

        long clientId = 1L;
        ClientDTO clientDTO = ClientDTO.builder().id(1L).firstName("Client").lastName("Name").build();
        when(clientService.getById(clientId)).thenReturn(clientDTO);
        doNothing().when(clientService).updateClient(clientDTO);

        mockMvc.perform(put("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"firstName\": \"Client\", \"lastName\": \"Name\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientId))
                .andExpect(jsonPath("$.firstName").value("Client"));
    }

    @Test
    void updateClient_ShouldReturnNotFound_WhenClientDoesNotExist() throws Exception {

        long clientId = 1L;
        when(clientService.getById(clientId)).thenThrow(new EntityNotFoundException("Client not found"));

        mockMvc.perform(put("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"firstName\": \"Client\", \"lastName\": \"Name\"}"))
                .andExpect(content().string("Client not found"));
    }

    @Test
    void deleteClient_ShouldReturnOk_WhenClientIsDeleted() throws Exception {

        long clientId = 1L;
        when(clientService.getById(clientId)).thenReturn(ClientDTO.builder().id(1L).firstName("Client").lastName("Name").build());
        doNothing().when(clientService).deleteClient(clientId);

        mockMvc.perform(delete("/clients/{id}", clientId))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted client successfully: " + clientId));
    }

    @Test
    void deleteClient_ShouldReturnNotFound_WhenClientDoesNotExist() throws Exception {

        long clientId = 1L;
        when(clientService.getById(clientId)).thenThrow(new EntityNotFoundException("Client not found"));

        mockMvc.perform(delete("/clients/{id}", clientId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Client not found"));
    }
}