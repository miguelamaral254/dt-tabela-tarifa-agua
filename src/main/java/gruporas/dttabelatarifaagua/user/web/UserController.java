package gruporas.dttabelatarifaagua.user.web;

import gruporas.dttabelatarifaagua.user.core.CreateUserUseCase;
import gruporas.dttabelatarifaagua.user.web.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody CreateUserRequest request) {
        UUID userId = createUserUseCase.execute(request);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }
}
