package dreamcloud.backend.Plan;

import dreamcloud.backend.Plan.model.Plan;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PlanMapper {

    @Select("select * from plans order by id")
    List<Plan> findAll();

    @Select("select * from plans where id = #{id}")
    Plan findById(Long id);

    @Select("select *from plans where name = #{name}")
    Plan findByName(String name);
}
