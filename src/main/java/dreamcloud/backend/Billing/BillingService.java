package dreamcloud.backend.Billing;

import dreamcloud.backend.Billing.model.*;
import dreamcloud.backend.Plan.PlanMapper;
import dreamcloud.backend.Plan.model.Plan;
import dreamcloud.backend.User.UserMapper;
import dreamcloud.backend.User.model.User;
import dreamcloud.backend.common.context.UserContext;
import dreamcloud.backend.common.exception.BusinessException;
import dreamcloud.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BillingService {
    private final SubscriptionRecordMapper subscriptionRecordMapper;
    private final PlanMapper planMapper;
    private final UserMapper userMapper;
    private final QuotaPeriodMapper quotaPeriodMapper;
    private final UsageRecordMapper usageRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public void subscribe(SubscribeDTO dto){
        User user = userMapper.findById(UserContext.getUserId());
        Plan plan = planMapper.findById(dto.planId());
        //更新用户套餐和余额
        userMapper.updatePlan(user.getId(),plan.getId());
        Long balanceAfter = user.getBalanceCents() - plan.getPriceCents();
        if (balanceAfter<0) throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE);
        userMapper.updateBalance(user.getId(),balanceAfter);

        //创建套餐周期
        QuotaPeriod quotaPeriod = new QuotaPeriod();
        quotaPeriod.setPlanId(plan.getId());
        quotaPeriod.setUserId(user.getId());
        quotaPeriod.setPeriodStart(LocalDateTime.now());
        quotaPeriod.setQuotaLimit(plan.getMonthlyQuota());
        quotaPeriod.setQuotaUsed(0L);
        quotaPeriod.setCreateTime(LocalDateTime.now());
        quotaPeriod.setPeriodEnd(LocalDateTime.now().plusDays(30));
        quotaPeriodMapper.insert(quotaPeriod);
        //记录订阅
        SubscriptionRecord subscriptionRecord = new SubscriptionRecord();
        subscriptionRecord.setPlanId(dto.planId());
        subscriptionRecord.setPeriodId(quotaPeriod.getId());
        subscriptionRecord.setUserId(UserContext.getUserId());
        subscriptionRecord.setBalanceAfter(balanceAfter);
        subscriptionRecord.setPriceCents(plan.getPriceCents());
        subscriptionRecord.setCreateTime(LocalDateTime.now());
        subscriptionRecordMapper.insert(subscriptionRecord);
    }

    public QuotaVO quota(Long userId){
        return QuotaVO.from(quotaPeriodMapper.findCurrent(userId));
    }

    @Transactional(rollbackFor = Exception.class)
    public QuotaVO consume(ConsumeDTO dto){
        //TODO mock测试扣除额度后面换实际的业务
        Long cost = 10L;
        Long userId = UserContext.getUserId();

        QuotaPeriod curr = quotaPeriodMapper.findCurrent(userId);
        if (curr == null) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_QUOTA);
        }

        //判断和扣减由数据库在同一条语句里完成
        int rows = quotaPeriodMapper.addUsed(curr.getId(), cost);
        if (rows == 0) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_QUOTA);
        }

        //扣减成功后重新读取，避免用旧快照计算剩余
        QuotaPeriod updated = quotaPeriodMapper.findCurrent(userId);

        UsageRecord usageRecord = new UsageRecord();
        usageRecord.setAmount(cost);
        usageRecord.setPeriodId(updated.getId());
        usageRecord.setQuotaRemainingAfter(updated.getQuotaLimit() - updated.getQuotaUsed());
        usageRecord.setReason(dto.action());
        usageRecord.setRefId(null);
        usageRecord.setUserId(userId);
        usageRecord.setCreateTime(LocalDateTime.now());
        usageRecordMapper.insert(usageRecord);

        return QuotaVO.from(updated);
    }

}
