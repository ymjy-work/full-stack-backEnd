package dreamcloud.backend.services;

import dreamcloud.backend.common.result.Result;
import dreamcloud.backend.mapper.UserMapper;
import dreamcloud.backend.model.entry.User;
import dreamcloud.backend.model.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public Result<UserInfoVO> profile(Long id){
        User user = userMapper.findById(id);
        if(user == null){
            return Result.fail(404,"用户不存在");
        }
        return Result.success(UserInfoVO.from(user));
    }
}
