package gruporas.dttabelatarifaagua.auth.core.model;

import java.util.UUID;

public record UserBasic(
    UUID id, 
    String username, 
    String email, 
    String firstName, 
    String lastName
) {}
