package gruporas.dttabelatarifaagua.tariff.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TariffCalculationRequest(
    @JsonProperty("categoria")
    String category,
    @JsonProperty("consumo")
    Integer consumption
) {}
