package dreamcloud.backend.model.dto;

import dreamcloud.backend.model.entry.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;

    public static LoginRequest from(User user){
        LoginRequest dto = new LoginRequest();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        return dto;
    }
}
