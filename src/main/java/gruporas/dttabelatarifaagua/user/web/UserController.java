package gruporas.dttabelatarifaagua.user.web;

import gruporas.dttabelatarifaagua.shared.pagination.PageResult;
import gruporas.dttabelatarifaagua.shared.pagination.Pageable;
import gruporas.dttabelatarifaagua.user.core.usecases.CreateUserUseCase;
import gruporas.dttabelatarifaagua.user.core.usecases.GetUserByIdUseCase;
import gruporas.dttabelatarifaagua.user.core.usecases.ListUsersUseCase;
import gruporas.dttabelatarifaagua.user.core.model.UserFilter;
import gruporas.dttabelatarifaagua.user.persistence.model.Role;
import gruporas.dttabelatarifaagua.user.web.dto.CreateUserRequest;
import gruporas.dttabelatarifaagua.user.web.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;

    @PostMapping
    public ResponseEntity<UUID> create(@Valid @RequestBody CreateUserRequest request) {
        UUID userId = createUserUseCase.execute(request);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResult<UserResponse>> list(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        var filter = new UserFilter(role, new Pageable(pageNumber, pageSize));
        var result = listUsersUseCase.execute(filter);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        var response = getUserByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}
