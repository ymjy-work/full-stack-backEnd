package dreamcloud.backend.Plan;

import dreamcloud.backend.Plan.model.Plan;
import dreamcloud.backend.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public Result<List<Plan>> list() {
        return planService.list();
    }
}
