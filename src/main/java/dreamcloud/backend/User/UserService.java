package dreamcloud.backend.User;

import dreamcloud.backend.User.model.User;
import dreamcloud.backend.User.model.UserInfoVO;
import dreamcloud.backend.common.context.UserContext;
import dreamcloud.backend.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public Result<UserInfoVO> profile(){
        User user = userMapper.findById(UserContext.getUserId());
        if(user == null){
            return Result.fail(404,"用户不存在");
        }
        return Result.success(UserInfoVO.from(user));
    }
}
