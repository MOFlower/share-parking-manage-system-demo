package top.moflowerlkh.shareparkingdemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.moflowerlkh.shareparkingdemo.model.ParkingInfo;
import top.moflowerlkh.shareparkingdemo.service.ParkingInfoService;

@Repository
public interface ParkingInfoDao extends JpaRepository<ParkingInfo, Long> {
    ParkingInfo findByParkingName(String parkingName);

    ParkingInfo findByClientSendTopic(String topic);
}
