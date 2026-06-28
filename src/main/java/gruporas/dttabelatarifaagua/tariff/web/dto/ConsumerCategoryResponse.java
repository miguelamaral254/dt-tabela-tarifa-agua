package gruporas.dttabelatarifaagua.tariff.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record ConsumerCategoryResponse(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("nome")
    String name
) {}
