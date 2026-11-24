package dreamcloud.backend.User;

import dreamcloud.backend.User.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    @Select("select * from users where username = #{username}")
    User findByUsername(String username);

    @Select("select * from users where id = #{id}")
    User findById(Long id);

    @Insert("INSERT INTO users(username, password, plan_id,balance_cents) "+
            "VALUES (#{username},#{password},#{planId},#{balanceCents})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insert(User user);

    @Update("update users set plan_id = #{planId},update_time = datetime('now','localtime') where id = #{userId}")
    int updatePlan(Long userId,Long planId);

    @Update("update users set balance_cents = #{balanceCents},update_time = datetime('now','localtime') where id =#{userId}")
    int updateBalance(Long userId, Long balanceCents);
}
