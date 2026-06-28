package gruporas.dttabelatarifaagua.tariff.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.UUID;

public record ConsumptionRangeResponse(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("categoria")
    ConsumerCategoryResponse consumerCategory,
    @JsonProperty("inicio")
    Integer start,
    @JsonProperty("fim")
    Integer end,
    @JsonProperty("valorUnitario")
    BigDecimal unitValue
    ) {}
