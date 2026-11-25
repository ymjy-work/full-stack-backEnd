package dreamcloud.backend.Billing;

import dreamcloud.backend.Billing.model.QuotaPeriod;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

public interface QuotaPeriodMapper {

    @Select("select * from quota_period where user_id = #{userId} " +
            "and period_end > NOW() " +
            "order by period_start desc limit 1")
    QuotaPeriod findCurrent(@Param("userId") Long userId);

    @Update("update quota_period set quota_used = quota_used + #{amount} " +
            "where id = #{periodId} and quota_used + #{amount} <= quota_limit")
    int addUsed(@Param("periodId") Long periodId, @Param("amount") Long amount);

    @Update("update quota_period set period_end = #{periodEnd} where id = #{id}")
    int updatePeriodEndById(Long id , LocalDateTime periodEnd);

    @Insert("insert into quota_period (user_id, plan_id, quota_limit, period_start, period_end) "
            + "values (#{userId},#{planId},#{quotaLimit},#{periodStart},#{periodEnd})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(QuotaPeriod quotaPeriod);
}
