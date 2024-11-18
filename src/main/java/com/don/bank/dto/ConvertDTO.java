package com.don.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ConvertDTO {

    @JsonProperty("base_code")
    private String baseCode;

    @JsonProperty("conversion_rates")
    private Map<String, Double> convertRates;

}
