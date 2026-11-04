package dreamcloud.backend.mapper;

import dreamcloud.backend.model.entry.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("select * from users where username = #{username}")
    User findByUsername(String username);

    @Select("select * from users where id = #{id}")
    User findById(Long id);
}
