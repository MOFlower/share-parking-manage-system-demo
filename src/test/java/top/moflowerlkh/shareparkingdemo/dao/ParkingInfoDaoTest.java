package top.moflowerlkh.shareparkingdemo.dao;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.moflowerlkh.shareparkingdemo.model.ParkingInfo;

import java.util.Optional;

@SpringBootTest
public class ParkingInfoDaoTest {
    @Autowired
    ParkingInfoDao parkingInfoDao;

    @Test
    public void findByIdTest() {
        Optional<ParkingInfo> p = parkingInfoDao.findById(Long.valueOf("2"));
        System.out.println(p.get().getClientReceiveTopic());
    }
}
