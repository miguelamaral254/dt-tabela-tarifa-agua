package gruporas.dttabelatarifaagua.tariff.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record TariffCalculationDetailResponse(
    @JsonProperty("faixa")
    RangeResponse faixa,
    @JsonProperty("m3Cobrados")
    Integer consumedM3,
    @JsonProperty("valorUnitario")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    BigDecimal unitValue,
    @JsonProperty("subtotal")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    BigDecimal subtotal
) {}
