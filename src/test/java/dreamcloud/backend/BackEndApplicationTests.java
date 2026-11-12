package dreamcloud.backend;

import dreamcloud.backend.Auth.LoginTokenMapper;
import dreamcloud.backend.User.model.User;
import dreamcloud.backend.User.UserMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BackEndApplicationTests {
    @Autowired
    private  UserMapper userMapper;
    @Autowired
    private  LoginTokenMapper loginTokenMapper;

    @Test
    void findByUsername(){
        User user = userMapper.findByUsername("admin");
        System.out.println(user);
        assertNotNull(user, "users 表里应该存在 admin");
    }

    @Test
    void bcrypt(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("123456");
        System.out.println("hash = "+hash);
        System.out.println("matches = "+encoder.matches("123456",hash));
    }

}
