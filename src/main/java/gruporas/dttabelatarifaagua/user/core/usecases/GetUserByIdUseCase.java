package gruporas.dttabelatarifaagua.user.core.usecases;

import gruporas.dttabelatarifaagua.shared.exception.ResourceNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.user.persistence.repository.UserRepository;
import gruporas.dttabelatarifaagua.user.web.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetUserByIdUseCase implements UseCase<UUID, UserResponse> {

    private final UserRepository userRepository;

    @Override
    public UserResponse execute(UUID id) {
        return userRepository.findById(id)
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getCpf(), u.getFirstName(), u.getLastName(), u.getRole()))
                .orElseThrow(() -> new ResourceNotFoundException("user.notFound"));
    }
}
