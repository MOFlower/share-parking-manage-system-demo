package top.moflowerlkh.shareparkingdemo.Service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.moflowerlkh.shareparkingdemo.dao.ParkingInfoDao;
import top.moflowerlkh.shareparkingdemo.model.Car;
import top.moflowerlkh.shareparkingdemo.model.ParkingInfo;
import top.moflowerlkh.shareparkingdemo.model.User;
import top.moflowerlkh.shareparkingdemo.service.Impl.CarServiceImpl;
import top.moflowerlkh.shareparkingdemo.service.Impl.UserServiceImpl;

@SpringBootTest
public class MockTest {
    @Autowired
    ParkingInfoDao parkingInfoDao;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    CarServiceImpl carService;

    @Test
    void MockTest() {
        String username = "helloworld";
        userService.addUser(username, "123");
        User u = userService.getUserByName("helloworld");


        ParkingInfo p = new ParkingInfo();
        p.setLatitude(35);
        p.setLongitude(116);
        p.setOwner(u);
        p.setParkingName("parkingA");
        parkingInfoDao.save(p);

        Car c = new Car();
        c.setCarNumber("carA");
        c.setOwner(u);
        carService.addCarInfo(c.getCarNumber(), u);

        userService.addParkingInfo(username, p.getParkingName());
        userService.addCarInfo(username, c.getCarNumber());
    }
}
