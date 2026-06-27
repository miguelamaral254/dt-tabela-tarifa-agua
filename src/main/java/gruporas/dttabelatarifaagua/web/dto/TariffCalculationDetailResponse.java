package gruporas.dttabelatarifaagua.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record TariffCalculationDetailResponse(
    @JsonProperty("faixa")
    RangeResponse faixa,
    @JsonProperty("m3Cobrados")
    Integer consumedM3,
    @JsonProperty("valorUnitario")
    BigDecimal unitValue,
    @JsonProperty("subtotal")
    BigDecimal subtotal
) {}
