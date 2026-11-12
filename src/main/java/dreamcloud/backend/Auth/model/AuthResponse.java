package dreamcloud.backend.Auth.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import dreamcloud.backend.User.model.UserInfoVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    @JsonUnwrapped
    private UserInfoVO userInfoVO;
}
