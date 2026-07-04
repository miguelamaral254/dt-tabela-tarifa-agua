package gruporas.dttabelatarifaagua.auth.core.usecases;

import gruporas.dttabelatarifaagua.auth.core.model.UserBasic;
import gruporas.dttabelatarifaagua.shared.exception.ResourceNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.NullaryUseCase;
import gruporas.dttabelatarifaagua.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
import gruporas.dttabelatarifaagua.shared.utils.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAuthenticatedUserUseCase implements NullaryUseCase<UserBasic> {

    private final UserRepository userRepository;

    @Override
    public UserBasic execute() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ObjectUtils.requireNonNull(authentication, "auth.unauthenticated");
        if (!authentication.isAuthenticated()) {
            throw new AccessDeniedException("Unauthenticated user");
        }

        UUID userId = UUID.fromString(authentication.getPrincipal().toString());
        
        return userRepository.findById(userId)
                .map(u -> new UserBasic(u.getId(), u.getUsername(), u.getEmail(), u.getFirstName(), u.getLastName()))
                .orElseThrow(() -> new ResourceNotFoundException("user.notFound"));
    }
}

