package dreamcloud.backend.Billing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsageRecord {
    private Long id;
    private Long userId;
    private Long periodId;
    private Long amount;
    private Long quotaRemainingAfter;
    private String reason;
    private Long refId;
    private LocalDateTime createTime;
}
