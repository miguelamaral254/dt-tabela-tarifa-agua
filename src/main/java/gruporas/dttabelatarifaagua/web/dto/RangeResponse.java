package gruporas.dttabelatarifaagua.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RangeResponse(
    @JsonProperty("inicio")
    Integer start,
    @JsonProperty("fim")
    Integer end
) {}
