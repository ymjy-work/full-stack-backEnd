package dreamcloud.backend.Auth;

import dreamcloud.backend.Auth.model.LoginToken;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface LoginTokenMapper {
    @Insert("INSERT INTO login_token(user_id,token,expires_time)"+
            "VALUES (#{userId},#{token},#{expiresTime})"
    )
    int insert(LoginToken loginToken);

    @Select("SELECT *FROM login_token WHERE token=#{token}")
    LoginToken findByToken(String token);

    @Delete("DELETE FROM login_token WHERE token = #{token}")
    int deleteByToken(String token);
}
