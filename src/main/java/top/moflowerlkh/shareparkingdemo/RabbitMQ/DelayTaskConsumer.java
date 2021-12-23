package top.moflowerlkh.shareparkingdemo.RabbitMQ;

import com.mysql.cj.log.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.moflowerlkh.shareparkingdemo.Common.TimeTaskMsg;
import top.moflowerlkh.shareparkingdemo.service.ParkingTimePlanService;

@Component
@Slf4j
public class DelayTaskConsumer {
    @Autowired
    ParkingTimePlanService parkingTimePlanService;

    @RabbitHandler
    @RabbitListener(queues = "share.parking.delay.ttl")
    public void handle(String msg) {
        log.info("delay task triggered\n" + msg.toString());
        TimeTaskMsg timeTaskMsg = TimeTaskMsg.getTimeTaskMsgFromString(msg);
        parkingTimePlanService.tryCancelTimePlanTask(timeTaskMsg);
    }
}
