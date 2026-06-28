package gruporas.dttabelatarifaagua.auth.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginRequest(
    @JsonProperty("email")
    String email,
    @JsonProperty("senha")
    String password
) {}
