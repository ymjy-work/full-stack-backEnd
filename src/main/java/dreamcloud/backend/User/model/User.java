package dreamcloud.backend.User.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private Long balanceCents;
    private Long planId;
    private String avatarUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
