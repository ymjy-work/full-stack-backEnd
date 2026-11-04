package dreamcloud.backend;

import dreamcloud.backend.mapper.UserMapper;
import dreamcloud.backend.model.entry.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BackEndApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    void findByUsername(){
        User user = userMapper.findByUsername("admin");
        System.out.println(user);
        assertNotNull(user, "users 表里应该存在 admin");
    }

}
