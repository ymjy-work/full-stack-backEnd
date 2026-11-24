package dreamcloud.backend.Billing;

import dreamcloud.backend.Billing.model.SubscriptionRecord;
import org.apache.ibatis.annotations.Insert;

public interface SubscriptionRecordMapper {
    @Insert("insert into subscription_record (user_id, plan_id, price_cents, balance_after, period_id, create_time) "
    +"values (#{userId},#{planId},#{priceCents},#{balanceAfter},#{periodId},#{createTime})")
    int insert(SubscriptionRecord subscriptionRecord);
}
