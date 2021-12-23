package top.moflowerlkh.shareparkingdemo.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.moflowerlkh.shareparkingdemo.Common.ParkingTimeTaskMsg;
import top.moflowerlkh.shareparkingdemo.service.ParkingTimePlanService;

@SpringBootTest
public class ParkingTimePlanServiceTest {
    @Autowired
    ParkingTimePlanService parkingTimePlanService;

    @Test
    void tryCancelTimeTaskTest() throws InterruptedException {
        ParkingTimeTaskMsg msg = new ParkingTimeTaskMsg("20:00:00", "21:03:00", "2");
        parkingTimePlanService.submitTimePlanTask(msg);
        parkingTimePlanService.tryCancelTimePlanTask(msg);
        parkingTimePlanService.cancelTimePlanTaskFinally(msg);
        Thread.sleep(150000);
    }

}
