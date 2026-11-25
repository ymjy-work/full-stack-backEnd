package dreamcloud.backend.Plan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {
    private Long id;
    private String name;
    private String description;
    private Long priceCents;
    private Long monthlyQuota;
    private int duration_days;
    private int status;
    private int level;
}
