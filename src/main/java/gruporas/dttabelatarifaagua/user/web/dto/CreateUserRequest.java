package gruporas.dttabelatarifaagua.user.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gruporas.dttabelatarifaagua.shared.validation.ValidRole;
import gruporas.dttabelatarifaagua.user.persistence.model.Role;

public record CreateUserRequest(
    @JsonProperty("nomeUsuario")
    String username,
    @JsonProperty("email")
    String email,
    @JsonProperty("cpf")
    String cpf,
    @JsonProperty("senha")
    String password,
    @JsonProperty("primeiroNome")
    String firstName,
    @JsonProperty("ultimoNome")
    String lastName,
    @ValidRole
    @JsonProperty("perfil")
    Role role
) {}
