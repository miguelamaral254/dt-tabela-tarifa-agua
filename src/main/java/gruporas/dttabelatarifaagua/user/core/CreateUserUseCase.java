package gruporas.dttabelatarifaagua.user.core;

import gruporas.dttabelatarifaagua.shared.exception.ValidationException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.shared.utils.ObjectUtils;
import gruporas.dttabelatarifaagua.user.persistence.model.User;
import gruporas.dttabelatarifaagua.user.persistence.repository.UserRepository;
import gruporas.dttabelatarifaagua.user.web.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CreateUserUseCase implements UseCase<CreateUserRequest, UUID> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UUID execute(CreateUserRequest request) {
        validateRequest(request);
        
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .cpf(request.cpf())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(request.role())
                .build();
        
        return userRepository.save(user).getId();
    }

    private void validateRequest(CreateUserRequest request) {
        ObjectUtils.requireNonNull(request, "user.notNull");
        if (request.email() == null || !request.email().endsWith("@gruporas.com.br")) {
            throw new ValidationException("user.email.invalidDomain");
        }
    }
}
