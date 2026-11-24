package dreamcloud.backend.Billing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRecord {
    private Long id;
    private Long userId;
    private Long planId;
    private Long balanceAfter;
    private Long periodId;
    private Long priceCents;
    private LocalDateTime createTime;
}
