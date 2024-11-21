package com.don.bank.service;

import com.don.bank.dto.ConvertDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConvertorCurrencyServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;


    @InjectMocks
    private ConvertorCurrencyService convertorCurrencyService;

    @Test
    void convert_shouldReturnConvertedAmount() {

        String from = "USD";
        String to = "RUB";
        double amount = 100.0;
        double conversionRate = 0.01;

        ConvertDTO convertDTO = ConvertDTO.builder()
                .baseCode("USD")
                .convertRates(Map.of(to, conversionRate))
                .build();

        String url = null;

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(url + from)).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(ConvertDTO.class)).thenReturn(ResponseEntity.ok(convertDTO));

        double result = convertorCurrencyService.convert(amount, from, to);

        assertEquals(1.0, result, 0.01);
        verify(restClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(url + from);
    }

}