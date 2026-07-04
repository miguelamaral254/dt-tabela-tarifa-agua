package gruporas.dttabelatarifaagua.user.core.usecases;

import gruporas.dttabelatarifaagua.shared.pagination.PageResult;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.user.core.model.UserFilter;
import gruporas.dttabelatarifaagua.user.persistence.model.User;
import gruporas.dttabelatarifaagua.user.persistence.repository.UserRepository;
import gruporas.dttabelatarifaagua.user.web.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ListUsersUseCase implements UseCase<UserFilter, PageResult<UserResponse>> {

    private final UserRepository userRepository;

    @Override
    public PageResult<UserResponse> execute(UserFilter filter) {
        log.info("Buscando usuários com perfil: {}", filter.getRole());
        try {
            String roleName = filter.getRole() != null ? filter.getRole().name() : null;
            int pageSize = filter.getPageable().pageSize();
            int offset = filter.getPageable().offset();

            java.util.List<User> users = userRepository.findAllFiltered(roleName, pageSize, offset);
            long totalElements = userRepository.countAllFiltered(roleName);

            var content = users.stream()
                    .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getCpf(), u.getFirstName(), u.getLastName(), u.getRole()))
                    .toList();
            
            int totalPages = (int) Math.ceil((double) totalElements / pageSize);

            return new PageResult<>(
                    content,
                    filter.getPageable().pageNumber(),
                    totalPages,
                    totalElements,
                    pageSize,
                    filter.getPageable().pageNumber() == 0,
                    filter.getPageable().pageNumber() >= totalPages - 1
            );
        } catch (Exception e) {
            log.error("Erro ao listar usuários", e);
            throw e;
        }
    }
}
