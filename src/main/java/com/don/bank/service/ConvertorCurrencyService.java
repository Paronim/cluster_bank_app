package com.don.bank.service;

import com.don.bank.dto.ConvertDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Service
public class ConvertorCurrencyService {

    private final RestClient restClient;

    @Value("${currency.api.url}")
    private String url;

    //URL новой API
    @Value("${currency.api.url.new")
    private String urlNew;

    //токен новой API
    @Value("${currency.api.token}")
    private String token;

    public ConvertorCurrencyService(RestClient restClient) {
        this.restClient = restClient;
    }

    public double convert(double amount, String from, String to) {

        try {
            //отдает ошибку при запросе base url, который отличается от USD
//            ResponseEntity<ConvertDTO> responseNew = restClient.get()
//                    .uri(uriBuilder -> uriBuilder
//                            .path(urlNew)
//                            .queryParam("app_id", token)
//                            .queryParam("base", from)
//                            .build())
//                    .retrieve()
//                    .toEntity(ConvertDTO.class);

            ResponseEntity<ConvertDTO> response = restClient.get().uri(url + from).retrieve().toEntity(ConvertDTO.class);

            return Objects.requireNonNull(response.getBody()).getConvertRates().get(to) * amount;

        } catch (Exception e) {
            throw new RuntimeException("Error during currency conversion: " + e.getMessage(), e);
        }
    }
}
