package gruporas.dttabelatarifaagua.tariff.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.UUID;

public record TariffTableSummaryResponse(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("nome")
    String name,
    @JsonProperty("dataVigencia")
    LocalDate effectiveDate,
    @JsonProperty("criadoPor")
    CreatedByResponse createdBy
) {}
