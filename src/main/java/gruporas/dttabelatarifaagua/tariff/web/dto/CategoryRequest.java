package gruporas.dttabelatarifaagua.tariff.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CategoryRequest(
    @JsonProperty("nome")
    String name,
    @JsonProperty("faixas")
    List<RangeRequest> ranges
) {}
