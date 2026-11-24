package dreamcloud.backend.Billing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotaPeriod {
    private Long id;
    private Long userId;
    private Long planId;
    private Long quotaUsed;
    private Long quotaLimit;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private LocalDateTime createTime;
}
