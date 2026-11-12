package dreamcloud.backend.Auth;

import dreamcloud.backend.Auth.model.AuthResponse;
import dreamcloud.backend.Auth.model.LoginRequest;
import dreamcloud.backend.Auth.model.RegisterRequest;
import dreamcloud.backend.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Result<AuthResponse> register(@RequestBody RegisterRequest dto){
        return Result.success(authService.register(dto));
    }

    @PostMapping ("/login")
    public Result<AuthResponse> login(@RequestBody LoginRequest dto){
        return Result.success(authService.login(dto));
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String authorization){
        String token = authorization.substring(7);
        authService.logout(token);
        return Result.success();
    }
}
