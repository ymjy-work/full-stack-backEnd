package dreamcloud.backend.Auth;

import dreamcloud.backend.Auth.model.AuthResponse;
import dreamcloud.backend.Auth.model.LoginRequest;
import dreamcloud.backend.Auth.model.LoginToken;
import dreamcloud.backend.Auth.model.RegisterRequest;
import dreamcloud.backend.User.model.User;
import dreamcloud.backend.User.model.UserInfoVO;
import dreamcloud.backend.User.UserMapper;
import dreamcloud.backend.common.utils.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final LoginTokenMapper loginTokenMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    private static final int TOKEN_EXPIRES_DAYS = 7;
    private static final Long DEFAULT_PLAN_ID = 1L;

    private String createToken(User user){
        LoginToken loginToken = new LoginToken();
        loginToken.setUserId(user.getId());
        loginToken.setToken(tokenGenerator.generate());
        loginToken.setExpiresTime(LocalDateTime.now().plusDays(TOKEN_EXPIRES_DAYS));
        loginTokenMapper.insert(loginToken);
        return  loginToken.getToken();
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthResponse register(RegisterRequest dto){
        if (userMapper.findByUsername(dto.getUsername())!=null){
            throw  new RuntimeException("用户已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPlanId(DEFAULT_PLAN_ID);
        userMapper.insert(user);

        return new AuthResponse(createToken(user),UserInfoVO.from(userMapper.findByUsername(user.getUsername())));
    }

    public AuthResponse login(LoginRequest dto){
        User user = userMapper.findByUsername(dto.getUsername());
        if(user == null || !passwordEncoder.matches(dto.getPassword(),user.getPassword())){
            throw new RuntimeException("用户名/密码错误");
        }
        return new AuthResponse(createToken(user),UserInfoVO.from(user));
    }

    public void logout(String  token){
        loginTokenMapper.deleteByToken(token);
    }
}
