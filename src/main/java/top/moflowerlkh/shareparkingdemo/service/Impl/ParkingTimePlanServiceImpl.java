package top.moflowerlkh.shareparkingdemo.service.Impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.moflowerlkh.shareparkingdemo.Common.QueueEnum;
import top.moflowerlkh.shareparkingdemo.Common.TimeInterval;
import top.moflowerlkh.shareparkingdemo.Common.TimeTaskMsg;
import top.moflowerlkh.shareparkingdemo.dao.ParkingInfoDao;
import top.moflowerlkh.shareparkingdemo.model.ParkingInfo;
import top.moflowerlkh.shareparkingdemo.mqtt.MqttMsgSendHandler;
import top.moflowerlkh.shareparkingdemo.service.ParkingInfoService;
import top.moflowerlkh.shareparkingdemo.service.ParkingTimePlanService;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ParkingTimePlanServiceImpl implements ParkingTimePlanService {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MqttMsgSendHandler mqttMsgSendHandler;
    @Autowired
    ParkingInfoDao parkingInfoDao;
    @Autowired
    ParkingInfoService parkingInfoService;

    @Override
    public boolean submitTimePlanTask(TimeTaskMsg msg) {
        rabbitTemplate.convertAndSend(
                QueueEnum.QUEUE_TTL_TASK.getExchangeName(),
                QueueEnum.QUEUE_TTL_TASK.getRouteKeyName(),
                msg.toString(),
                data -> {
                    data.getMessageProperties().setDelay(msg.getDelayTime());
                    return data;
                }
        );
        log.info("task submitted!\n" + msg.toString());
        return true;
    }

    @Override
    public boolean tryCancelTimePlanTask(TimeTaskMsg msg) {
        Optional<ParkingInfo> p = parkingInfoDao.findById(Long.valueOf(msg.getMsg()));
        String clientReceiveTopic = p.get().getClientReceiveTopic();
        mqttMsgSendHandler.isEmpty(clientReceiveTopic, JSON.toJSONString(Map.of("msg", msg.toString())));
        return true;
    }

    @Override
    public boolean cancelTimePlanTaskFinally(TimeTaskMsg msg) {
        Optional<ParkingInfo> p = parkingInfoDao.findById(Long.valueOf(msg.getMsg()));
        parkingInfoService.setFreeTimeOfParking(
                p.get().getId(),
                new TimeInterval(msg.getBeginTime(), msg.getEndTime()));
        return true;
    }

    @Override
    public boolean continueTimePlanTask(TimeTaskMsg msg) {
        return true;
    }
}

