package top.moflowerlkh.shareparkingdemo.service;

import top.moflowerlkh.shareparkingdemo.Common.TimeInterval;
import top.moflowerlkh.shareparkingdemo.model.ParkingInfo;
import top.moflowerlkh.shareparkingdemo.model.User;

import java.util.List;
import java.util.Map;

public interface ParkingInfoService {
    Map<ParkingInfo, List<TimeInterval>> getAllParkingInfo();

    Map<ParkingInfo, List<TimeInterval>> getParkInfoOfUser(User u);

    List<TimeInterval> getFreeTimeOfParking(long parkingID);

    boolean setFreeTimeOfParking(long parkingID, TimeInterval timeInterval);

    boolean setUsingTimeOfParking(long parkingID, TimeInterval timeInterval);

    boolean carIsAvailable(String parkingID, String carNumber);

    Map<String, String> registerParkingInfo(String uniqueCode,
                                            String identityCode,
                                            String parkingName,
                                            double longitude,
                                            double latitude,
                                            User owner) throws Exception;
}
