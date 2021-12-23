package top.moflowerlkh.shareparkingdemo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.moflowerlkh.shareparkingdemo.service.Impl.UserServiceImpl;

import javax.servlet.http.HttpSession;

@Api(tags = "用户模块")
@RestController
public class UserController {
    @Autowired
    UserServiceImpl userService;


    @ApiOperation("注册用户")
    @PostMapping("/user/register")
    public String UserRegister(String username, String password) {
        if (userService.addUser(username, password)) {
            return "success";
        }
        return "failed";
    }

    @ApiOperation("用户登录")
    @PostMapping("/user/login")
    public String userLogin(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            @ApiIgnore HttpSession httpSession) {
        if (userService.getUserByName(username).getPassword().equals(password)) {
            httpSession.setAttribute("username", username);
            return "success";
        }
        return "failed";
    }
}