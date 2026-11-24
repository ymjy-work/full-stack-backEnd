package dreamcloud.backend.Billing;

import dreamcloud.backend.Billing.model.ConsumeDTO;
import dreamcloud.backend.Billing.model.QuotaVO;
import dreamcloud.backend.Billing.model.SubscribeDTO;
import dreamcloud.backend.common.context.UserContext;
import dreamcloud.backend.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingController {
    private final BillingService billingService;

    @PostMapping("/subscribe")
    public Result<Void> subscribe(@RequestBody SubscribeDTO dto){
        billingService.subscribe(dto);
        return Result.success();
    }

    @PostMapping("/consume")
    public Result<QuotaVO> consume(@RequestBody ConsumeDTO dto){
       return Result.success(billingService.consume(dto));
    }

    @GetMapping("/quota")
    public Result<QuotaVO> quota(){
        return Result.success(billingService.quota(UserContext.getUserId()));
    }


}