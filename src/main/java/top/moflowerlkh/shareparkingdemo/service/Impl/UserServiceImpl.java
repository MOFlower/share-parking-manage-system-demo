package top.moflowerlkh.shareparkingdemo.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.moflowerlkh.shareparkingdemo.dao.CarDao;
import top.moflowerlkh.shareparkingdemo.dao.ParkingInfoDao;
import top.moflowerlkh.shareparkingdemo.dao.UserDao;
import top.moflowerlkh.shareparkingdemo.model.Car;
import top.moflowerlkh.shareparkingdemo.model.ParkingInfo;
import top.moflowerlkh.shareparkingdemo.model.User;
import top.moflowerlkh.shareparkingdemo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    CarDao carDao;
    @Autowired
    ParkingInfoDao parkingInfoDao;

    @Override
    public boolean addUser(String username, String password) {
        User u = new User(username, password);
        userDao.save(u);
        return true;
    }

    @Override
    public User getUserByName(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public boolean addCarInfo(String username, String carNumber) {
        User u = userDao.findByUsername(username);
        Car c = carDao.findByCarNumber(carNumber);
        u.getOwningCar().add(c);
        userDao.save(u);
        return true;
    }

    @Override
    public boolean addParkingInfo(String username, String parkingInfoName) {
        User u = userDao.findByUsername(username);
        ParkingInfo p = parkingInfoDao.findByParkingName(parkingInfoName);
        if (u.getOwningParking().contains(p)) {
            return false;
        }
        u.getOwningParking().add(p);
        userDao.save(u);
        return true;
    }

}
