package gruporas.dttabelatarifaagua.user.web.dto;

import gruporas.dttabelatarifaagua.user.persistence.model.Role;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String email,
    String cpf,
    String firstName,
    String lastName,
    Role role
) {}
