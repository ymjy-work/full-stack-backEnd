package dreamcloud.backend.User;

import dreamcloud.backend.User.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("select * from users where username = #{username}")
    User findByUsername(String username);

    @Select("select * from users where id = #{id}")
    User findById(Long id);

    @Insert("INSERT INTO users(username, password, plan_id) "+
            "VALUES (#{username},#{password},#{planId})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insert(User user);
}
