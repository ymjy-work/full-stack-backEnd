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
    
    private  final static int PERIOD_DAYS = 30;

    @Transactional(rollbackFor = Exception.class)
    public QuotaVO upgradePlan(SubscribeDTO dto){
        Plan targetPlan = planMapper.findById(dto.planId());
        if(targetPlan == null) throw new BusinessException(ErrorCode.NOT_FOUND_PLAN);
        Long userId = UserContext.getUserId();
        QuotaPeriod currPeriod = currentPeriod(userId);
        Plan currPlan = planMapper.findById(currPeriod.getPlanId());
        //TODO "="情况一个是续费考虑还有一个避免free无限刷额度后续需要额外处理这2个情况
//        更新用户订阅
        if(targetPlan.getLevel()<=currPlan.getLevel()) throw new BusinessException(ErrorCode.PLAN_NOT_UPGRADABLE);
        int row = userMapper.updateBalance(userId, Long.valueOf(-targetPlan.getPriceCents()));
        if(row!=1){
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        userMapper.updatePlan(userId,targetPlan.getId());

        // 结束当前周期并且新建新订阅周期
        quotaPeriodMapper.updatePeriodEndById(currPeriod.getId(),LocalDateTime.now());
        QuotaPeriod newPeriod = createPeriod(userId,targetPlan);
        //记录订单
        recordSubscription(targetPlan, newPeriod);

        return QuotaVO.from(newPeriod);
    }

    public QuotaVO quota(Long userId){
        return QuotaVO.from(currentPeriod(userId));
    }

    @Transactional(rollbackFor = Exception.class)
    public QuotaVO consume(ConsumeDTO dto){
        //TODO mock测试扣除额度后面换实际的业务
        Long cost = Long.valueOf("10");
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
        usageRecord.setQuotaRemainingAfter(Long.valueOf(updated.getQuotaLimit() - updated.getQuotaUsed()));
        usageRecord.setReason(dto.action());
        usageRecord.setRefId(null);
        usageRecord.setUserId(userId);
        usageRecord.setCreateTime(LocalDateTime.now());
        usageRecordMapper.insert(usageRecord);

        return QuotaVO.from(updated);
    }

    private void recordSubscription(Plan targetPlan, QuotaPeriod newPeriod) {
        SubscriptionRecord subscriptionRecord = new SubscriptionRecord();
        subscriptionRecord.setPriceCents(targetPlan.getPriceCents());
        subscriptionRecord.setCreateTime(LocalDateTime.now());
        subscriptionRecord.setUserId(UserContext.getUserId());
        subscriptionRecord.setPeriodId(newPeriod.getId());
        subscriptionRecord.setPlanId(targetPlan.getId());
        subscriptionRecord.setBalanceAfter(userMapper.findById(UserContext.getUserId()).getBalanceCents());
        subscriptionRecordMapper.insert(subscriptionRecord);
    }

    public QuotaPeriod currentPeriod(Long userId){
        QuotaPeriod current = quotaPeriodMapper.findCurrent(userId);
        if (current!=null) return current;

        User user = userMapper.findById(userId);
        Plan free = planMapper.findByName("free");
        if(!free.getId().equals(user.getPlanId())){
            userMapper.updatePlan(userId,free.getId());
        }
        return createPeriod(userId,free);
    }

    public QuotaPeriod createPeriod(Long userId,Plan plan){
        QuotaPeriod quotaPeriod = new QuotaPeriod();
        quotaPeriod.setUserId(userId);
        quotaPeriod.setPlanId(plan.getId());
        quotaPeriod.setQuotaLimit(plan.getMonthlyQuota());
        quotaPeriod.setQuotaUsed(Long.valueOf("0"));
        quotaPeriod.setCreateTime(LocalDateTime.now());
        quotaPeriod.setPeriodStart(LocalDateTime.now());
        quotaPeriod.setPeriodEnd(LocalDateTime.now().plusDays(PERIOD_DAYS));
        quotaPeriodMapper.insert(quotaPeriod);
        return quotaPeriod;
    }
}
