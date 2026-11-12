package dreamcloud.backend.Auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import dreamcloud.backend.User.model.User;

@Data
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;

    public static LoginRequest from(User user){
        LoginRequest dto = new LoginRequest();
        BeanUtils.copyProperties(user,dto);
        return dto;
    }
}
