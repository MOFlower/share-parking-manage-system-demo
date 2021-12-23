package top.moflowerlkh.shareparkingdemo.service;

import org.springframework.scheduling.support.SimpleTriggerContext;
import top.moflowerlkh.shareparkingdemo.model.User;

public interface UserService {
    boolean addUser(String username, String password);

    User getUserByName(String username);

    boolean addCarInfo(String username, String carNumber);

    boolean addParkingInfo(String username, String parkingInfoName);
}
