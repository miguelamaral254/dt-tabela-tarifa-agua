package gruporas.dttabelatarifaagua.tariff.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record RangeRequest(
    @JsonProperty("inicio")
    Integer start,
    @JsonProperty("fim")
    Integer end,
    @JsonProperty("valorUnitario")
    BigDecimal unitValue
) {}
