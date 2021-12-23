package top.moflowerlkh.shareparkingdemo.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.moflowerlkh.shareparkingdemo.dao.CarDao;
import top.moflowerlkh.shareparkingdemo.dao.UserDao;
import top.moflowerlkh.shareparkingdemo.model.Car;
import top.moflowerlkh.shareparkingdemo.model.User;
import top.moflowerlkh.shareparkingdemo.service.CarService;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    CarDao carDao;
    @Autowired
    UserDao userDao;

    public boolean addCarInfo(String CarNumber, User u) {
        Car car = new Car();
        car.setCarNumber(CarNumber);
        car.setOwner(u);
        carDao.save(car);
        return true;
    }
}
