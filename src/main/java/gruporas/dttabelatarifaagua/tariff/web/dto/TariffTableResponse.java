
package gruporas.dttabelatarifaagua.tariff.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TariffTableResponse(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("nome")
    String name,
    @JsonProperty("dataVigencia")
    LocalDate effectiveDate,
    @JsonProperty("criadoPor")
    CreatedByResponse createdBy,
    @JsonProperty("faixasConsumo")
    List<ConsumptionRangeResponse> consumptionRanges
) {}
