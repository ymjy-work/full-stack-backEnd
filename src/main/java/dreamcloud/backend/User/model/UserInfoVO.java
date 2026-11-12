package dreamcloud.backend.User.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserInfoVO {
    private String username;
    private String avatarUrl;
    private Long planId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static UserInfoVO from(User user) {
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user,vo);
        return vo;
    }
}
