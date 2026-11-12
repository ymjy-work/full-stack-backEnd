package dreamcloud.backend.Auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginToken {
    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime createTime;
    private LocalDateTime expiresTime;
}
