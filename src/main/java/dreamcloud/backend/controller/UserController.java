package dreamcloud.backend.controller;

import dreamcloud.backend.common.result.Result;
import dreamcloud.backend.model.entry.User;
import dreamcloud.backend.model.vo.UserInfoVO;
import dreamcloud.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Result<UserInfoVO> user(@PathVariable Long id){
        return userService.profile(id);
    }
}
