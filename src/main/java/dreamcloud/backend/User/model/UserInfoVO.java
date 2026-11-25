package dreamcloud.backend.User.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public record UserInfoVO(
       String username,
       BigDecimal balance,
       String avatarUrl,
       LocalDateTime createTime,
       LocalDateTime updateTime
) {
    public static UserInfoVO from(User entity){
        if (entity==null) return null;
        BigDecimal balance = entity.getBalanceCents() == null
                ?BigDecimal.ZERO
                :BigDecimal.valueOf(entity.getBalanceCents())
                .divide(new BigDecimal(100),2, RoundingMode.DOWN);
        return new UserInfoVO(
                entity.getUsername(),
                balance,
                entity.getAvatarUrl(),
                entity.getCreateTime(),
                entity.getUpdateTime()
        );
    }
}
