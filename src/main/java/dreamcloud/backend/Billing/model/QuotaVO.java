package dreamcloud.backend.Billing.model;

import java.time.LocalDateTime;

public record QuotaVO(
        Long quotaLimit,
        Long quotaUsed,
        Long quotaRemaining,
        LocalDateTime periodEnd
) {
    public static QuotaVO from(QuotaPeriod entity){
        if(entity==null) return null;
        return new QuotaVO(
                entity.getQuotaLimit(),
                entity.getQuotaUsed(),
                entity.getQuotaLimit() - entity.getQuotaUsed(),
                entity.getPeriodEnd()
        );
    }
}
