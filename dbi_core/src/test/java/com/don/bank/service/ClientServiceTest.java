package com.don.bank.service;

import com.don.common.models.ClientDTO;
import com.don.bank.entity.Client;
import com.don.bank.repository.ClientRepository;
import com.don.bank.util.mappingUtils.MappingUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountService accountService;

    private MockedStatic<MappingUtils> mappingUtils;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        mappingUtils = mockStatic(MappingUtils.class);
    }

    @Test
    void getById_shouldThrowException_whenClientDoesNotExist() {
        long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> clientService.getById(clientId));
        assertEquals("Client with id " + clientId + " not found", exception.getMessage());
        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    void updateClient_shouldUpdateClientSuccessfully() {
        ClientDTO clientDTO = ClientDTO.builder().id(1L).build();
        Client client = Client.builder().id(1L).build();

        when(MappingUtils.mapToClient(clientDTO)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        when(MappingUtils.mapToClientDto(client)).thenReturn(clientDTO);

        clientService.updateClient(clientDTO);

        verify(clientRepository, times(1)).save(client);
        verify(clientRepository, times(1)).findById(client.getId());
    }

    @Test
    void deleteClient_shouldDeleteClientById() {
        long clientId = 1L;

        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @AfterEach
    public void tearDown() {
        mappingUtils.close();
    }

}