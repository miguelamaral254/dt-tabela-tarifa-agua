package gruporas.dttabelatarifaagua.tariff.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record CreatedByResponse(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("primeiroNome")
    String firstName,
    @JsonProperty("ultimoNome")
    String lastName
) {}
