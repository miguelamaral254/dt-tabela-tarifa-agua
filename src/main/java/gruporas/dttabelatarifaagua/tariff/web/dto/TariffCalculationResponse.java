package gruporas.dttabelatarifaagua.tariff.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public record TariffCalculationResponse(
    @JsonProperty("categoria")
    String category,
    @JsonProperty("consumoTotal")
    Integer totalConsumption,

    @JsonProperty("valorTotal")
    BigDecimal totalValue,
    @JsonProperty("detalhamento")
    List<TariffCalculationDetailResponse> details
) {}
