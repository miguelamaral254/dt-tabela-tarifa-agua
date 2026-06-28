package gruporas.dttabelatarifaagua.user.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gruporas.dttabelatarifaagua.user.persistence.model.Role;
import java.util.UUID;

public record UserResponse(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("email")
    String email,
    @JsonProperty("cpf")
    String cpf,
    @JsonProperty("primeiroNome")
    String firstName,
    @JsonProperty("ultimoNome")
    String lastName,
    @JsonProperty("perfil")
    Role role
) {}
