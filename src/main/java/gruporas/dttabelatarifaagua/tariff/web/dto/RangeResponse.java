package gruporas.dttabelatarifaagua.tariff.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record RangeResponse(
    @JsonProperty("inicio")
    Integer start,
    @JsonProperty("fim")
    Integer end
) {}
