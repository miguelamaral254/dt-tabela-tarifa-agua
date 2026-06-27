package gruporas.dttabelatarifaagua.user.web.dto;

import gruporas.dttabelatarifaagua.user.persistence.model.Role;

public record CreateUserRequest(
    String username,
    String email,
    String cpf,
    String password,
    String firstName,
    String lastName,
    Role role
) {}
