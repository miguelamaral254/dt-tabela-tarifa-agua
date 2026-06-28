package gruporas.dttabelatarifaagua.auth.core.usecases;

import gruporas.dttabelatarifaagua.auth.core.model.UserBasic;
import gruporas.dttabelatarifaagua.auth.core.ports.TokenProvider;
import gruporas.dttabelatarifaagua.shared.usecase.NullaryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAuthenticatedUserUseCase implements NullaryUseCase<UserBasic> {

    private final TokenProvider tokenProvider;

    @Override
    public UserBasic execute() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Unauthenticated user");
        }

        String token = authentication.getCredentials().toString();
        Map<String, Object> claims = tokenProvider.extractClaims(token);

        return new UserBasic(
                UUID.fromString((String) claims.get("oid")),
                (String) claims.get("username"),
                (String) claims.get("email"),
                (String) claims.get("firstName"),
                (String) claims.get("lastName")
        );
    }
}


