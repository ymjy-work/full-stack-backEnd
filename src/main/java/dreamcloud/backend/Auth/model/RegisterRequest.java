package dreamcloud.backend.Auth.model;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import dreamcloud.backend.User.model.User;

@Data
public class RegisterRequest {
    private String username;
    private String password;

    public static RegisterRequest from(User user){
        RegisterRequest dto = new RegisterRequest();
        BeanUtils.copyProperties(user,dto);
        return dto;
    }
}
