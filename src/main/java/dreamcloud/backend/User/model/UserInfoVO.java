package dreamcloud.backend.User.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UserInfoVO(
        String username,
        String avatarUrl,
        Long planId,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createTime,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updateTime
) {
    public static UserInfoVO from(User user) {
        if (user == null) {
            return null;
        }

        return new UserInfoVO(
                user.getUsername(),
                user.getAvatarUrl(),
                user.getPlanId(),
                user.getCreateTime(),
                user.getUpdateTime()
        );
    }
}
