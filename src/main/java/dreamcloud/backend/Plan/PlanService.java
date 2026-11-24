package dreamcloud.backend.Plan;

import dreamcloud.backend.Plan.model.PlanVO;
import dreamcloud.backend.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanMapper planMapper;

    public Result<List<PlanVO>> list() {
        return Result.success(
                planMapper.findAll()
                        .stream()
                        .filter(plan -> plan.getStatus() == 1)
                        .map(PlanVO::from)
                        .toList()
        );
    }
}
