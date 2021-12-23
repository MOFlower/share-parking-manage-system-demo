package top.moflowerlkh.shareparkingdemo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.moflowerlkh.shareparkingdemo.model.User;
import top.moflowerlkh.shareparkingdemo.service.Impl.CarServiceImpl;
import top.moflowerlkh.shareparkingdemo.service.Impl.UserServiceImpl;

import javax.servlet.http.HttpSession;

@Api(tags = "用户车辆信息管理")
@RestController
public class CarController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    CarServiceImpl carService;

    @ApiOperation("添加车辆信息")
    @PostMapping("/car")
    public String addCarInfo(@ApiIgnore HttpSession httpSession,
                             @RequestParam("carNumber") String carNumber) {
        String username = (String) httpSession.getAttribute("username");
        if (username == null) {
            return "not login";
        }
        User user = userService.getUserByName(username);
        carService.addCarInfo(carNumber, user);
        userService.addCarInfo(username, carNumber);
        return "success";
    }
}
