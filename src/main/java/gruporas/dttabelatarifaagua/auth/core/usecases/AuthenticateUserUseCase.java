package gruporas.dttabelatarifaagua.auth.core.usecases;

import gruporas.dttabelatarifaagua.auth.core.ports.TokenProvider;
import gruporas.dttabelatarifaagua.auth.web.dto.LoginRequest;
import gruporas.dttabelatarifaagua.shared.exception.ValidationException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase implements UseCase<LoginRequest, String> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Override
    public String execute(LoginRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ValidationException("user.notFound"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ValidationException("user.invalidCredentials");
        }

        Map<String, Object> claims = Map.of(
                "oid", user.getId().toString(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "role", user.getRole().name()
        );

        return tokenProvider.generateToken(claims, user.getUsername());
    }
}
