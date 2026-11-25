package dreamcloud.backend.Billing;

import dreamcloud.backend.Billing.model.SubscriptionRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface SubscriptionRecordMapper {
    @Insert("insert into subscription_record (user_id, plan_id, price_cents, balance_after, period_id, create_time) "
    +"values (#{userId},#{planId},#{priceCents},#{balanceAfter},#{periodId},#{createTime})")
    int insert(SubscriptionRecord subscriptionRecord);

    @Update("update subscription_record set plan_id=#{planID},balance_after = balance_after-#{cost} " +
            "where id =#{id} and balance_after-#{cost}>=0")
    int update(@Param("planId") Long id, @Param("id") Long planId, @Param("cost") Long cost);
}
