package gruporas.dttabelatarifaagua.auth.core.usecases;

import gruporas.dttabelatarifaagua.auth.core.ports.TokenProvider;
import gruporas.dttabelatarifaagua.auth.web.dto.LoginRequest;
import gruporas.dttabelatarifaagua.shared.exception.ValidationException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase implements UseCase<LoginRequest, String> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Override
    public String execute(LoginRequest request) {
        log.info("Tentando autenticar usuário: {}", request.email());
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: {}", request.email());
                    return new ValidationException("user.notFound");
                });

        boolean match = passwordEncoder.matches(request.password(), user.getPassword());

        if (!match) {
            throw new ValidationException("user.invalidCredentials");
        }

        Map<String, Object> claims = Map.of();

        return tokenProvider.generateToken(claims, user.getId().toString());
    }
}
