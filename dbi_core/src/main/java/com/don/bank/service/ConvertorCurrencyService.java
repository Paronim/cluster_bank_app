package com.don.bank.service;


import com.don.common.models.ConvertDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

/**
 * Service for currency conversion. Provides functionality to convert an amount
 * from one currency to another using an external API.
 */
@Service
public class ConvertorCurrencyService {

    private final RestClient restClient;

    @Value("${currency.api.url}")
    private String url;

    /**
     * Constructs the ConvertorCurrencyService with a RestClient.
     *
     * @param restClient the client used for making HTTP requests to the currency API
     */
    public ConvertorCurrencyService(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Converts an amount from one currency to another.
     *
     * @param amount the amount to be converted
     * @param from the source currency code (e.g., "USD")
     * @param to the target currency code (e.g., "RUB")
     * @return the converted amount in the target currency
     * @throws RuntimeException if an error occurs during the conversion process
     */
    public double convert(double amount, String from, String to) {
        try {

            ResponseEntity<ConvertDTO> response = restClient.get()
                    .uri(url + from)
                    .retrieve()
                    .toEntity(ConvertDTO.class);

            double result =  Objects.requireNonNull(response.getBody())
                    .getConvertRates()
                    .get(to) * amount;

            double scale = Math.pow(10, 3);
            return Math.ceil(result * scale) / scale;

        } catch (Exception e) {
            throw new RuntimeException("Error during currency conversion: " + e.getMessage(), e);
        }
    }
}
