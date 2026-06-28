package gruporas.dttabelatarifaagua.auth.web;

import gruporas.dttabelatarifaagua.auth.core.usecases.AuthenticateUserUseCase;
import gruporas.dttabelatarifaagua.auth.web.dto.LoginRequest;
import gruporas.dttabelatarifaagua.auth.web.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = authenticateUserUseCase.execute(request);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
