package dreamcloud.backend.Auth.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dreamcloud.backend.User.model.UserInfoVO;

public record AuthResponse(
        String token,
        @JsonUnwrapped
        UserInfoVO userInfo
) {
}
