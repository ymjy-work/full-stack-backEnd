package dreamcloud.backend.Auth.model;

public record LoginRequest(
        String username,
        String password
) {
}
