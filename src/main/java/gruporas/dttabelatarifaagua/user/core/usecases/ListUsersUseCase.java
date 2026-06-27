package gruporas.dttabelatarifaagua.user.core.usecases;

import gruporas.dttabelatarifaagua.shared.pagination.PageResult;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.user.core.model.UserFilter;
import gruporas.dttabelatarifaagua.user.persistence.model.User;
import gruporas.dttabelatarifaagua.user.persistence.repository.UserRepository;
import gruporas.dttabelatarifaagua.user.web.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ListUsersUseCase implements UseCase<UserFilter, PageResult<UserResponse>> {

    private final UserRepository userRepository;

    @Override
    public PageResult<UserResponse> execute(UserFilter filter) {
        Page<User> page = userRepository.findAll(filter.getPageable().toPageRequest());
        
        var content = page.getContent().stream()
                .filter(u -> filter.getUsername() == null || u.getUsername().contains(filter.getUsername()))
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getCpf(), u.getFirstName(), u.getLastName(), u.getRole()))
                .toList();

        return new PageResult<>(
                content,
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.isFirst(),
                page.isLast()
        );
    }
}
