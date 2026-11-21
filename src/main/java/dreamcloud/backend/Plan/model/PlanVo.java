package dreamcloud.backend.Plan.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record PlanVo(
        Long id,
        String name,
        String description,
        BigDecimal price
        ) {
    public static PlanVo from(Plan entity) {
        if (entity == null) {
            return null;
        }

        // 2. 在这里完成字段对应 + 单位换算（分 -> 元，保留2位小数）
        BigDecimal price = entity.getPriceCents() == null
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(entity.getPriceCents())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        return new PlanVo(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                price
        );
    }
}
