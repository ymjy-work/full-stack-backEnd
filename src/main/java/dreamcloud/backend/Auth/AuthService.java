package dreamcloud.backend.Auth;

import dreamcloud.backend.Auth.model.AuthResponse;
import dreamcloud.backend.Auth.model.LoginRequest;
import dreamcloud.backend.Auth.model.LoginToken;
import dreamcloud.backend.Auth.model.RegisterRequest;
import dreamcloud.backend.User.model.User;
import dreamcloud.backend.User.model.UserInfoVO;
import dreamcloud.backend.User.UserMapper;
import dreamcloud.backend.common.exception.BusinessException;
import dreamcloud.backend.common.exception.ErrorCode;
import dreamcloud.backend.common.utils.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final LoginTokenMapper loginTokenMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
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
        if (userMapper.findByUsername(dto.username())!=null){
            throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setPlanId(DEFAULT_PLAN_ID);
        userMapper.insert(user);

        return new AuthResponse(createToken(user),UserInfoVO.from(userMapper.findByUsername(user.getUsername())));
    }

    public AuthResponse login(LoginRequest dto){
        User user = userMapper.findByUsername(dto.username());
        if(user == null || !passwordEncoder.matches(dto.password(),user.getPassword())){
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
        return new AuthResponse(createToken(user),UserInfoVO.from(user));
    }

    public void logout(String  token){
        loginTokenMapper.deleteByToken(token);
    }
}
