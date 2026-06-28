package gruporas.dttabelatarifaagua.tariff.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public record TariffTableRequest(
    @JsonProperty("nome")
    String name,
    @JsonProperty("dataVigencia")
    LocalDate effectiveDate,
    @JsonProperty("categorias")
    List<CategoryRequest> categories
) {}
