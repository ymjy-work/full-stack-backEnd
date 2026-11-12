package dreamcloud.backend.Plan;

import dreamcloud.backend.Plan.model.Plan;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PlanMapper {

    @Select("select * from plan order by id")
    List<Plan> findAll();

    @Select("select * from plan where id = #{id}")
    Plan findById(Long id);
}
