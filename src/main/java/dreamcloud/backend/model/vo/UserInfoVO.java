package dreamcloud.backend.model.vo;

import dreamcloud.backend.model.entry.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoVO {
    private String username;
    private String avatarUrl;

    public static UserInfoVO from(User user) {
        UserInfoVO vo = new UserInfoVO();
        vo.setUsername(user.getUsername());
        vo.setAvatarUrl(user.getAvatarUrl());
        return vo;
    }
}
