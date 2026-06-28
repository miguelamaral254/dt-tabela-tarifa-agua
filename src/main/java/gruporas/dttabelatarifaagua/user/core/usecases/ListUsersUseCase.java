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

            Page<User> page = userRepository.findAllFiltered(roleName, filter.getPageable().toPageRequest());
            
            var content = page.getContent().stream()
                    .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getCpf(), u.getFirstName(), u.getLastName(), u.getRole()))
                    .toList();
            
            return new PageResult<>(
                    content, 
                    page.getNumber(), 
                    page.getTotalPages(), 
                    page.getTotalElements(), 
                    page.getSize(), 
                    page.isFirst(), 
                    page.isLast());
        } catch (Exception e) {
            log.error("Erro ao listar usuários", e);
            throw e;
        }
    }
}
