package dreamcloud.backend.Billing;

import dreamcloud.backend.Billing.model.UsageRecord;
import org.apache.ibatis.annotations.Insert;

public interface UsageRecordMapper {
    @Insert("insert into usage_record (user_id, period_id, amount, quota_remaining_after, reason, ref_id) "
    +"values (#{userId},#{periodId},#{amount},#{quotaRemainingAfter},#{reason},#{refId})")
    int insert(UsageRecord usageRecord);
}
