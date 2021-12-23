package top.moflowerlkh.shareparkingdemo.mqtt;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.moflowerlkh.shareparkingdemo.Common.ParkingTimeTaskMsg;
import top.moflowerlkh.shareparkingdemo.dao.ParkingInfoDao;
import top.moflowerlkh.shareparkingdemo.model.ParkingInfo;
import top.moflowerlkh.shareparkingdemo.service.ParkingInfoService;

import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class MqttTest {
    @Autowired
    IMqttSender iMqttSender;
    @Autowired
    MqttMsgSendHandler mqttMsgSendHandler;

    @Test
    public void TestMqttSend() {
        String testOpen = JSON.toJSONString(Map.of("method", "open"));
        iMqttSender.sendToMqtt("receive-topic", 0, testOpen);
    }

    @Test
    void TestMqttIsEmpty() {
//        ParkingTimeTaskMsg msg = new ParkingTimeTaskMsg("20:00:00", "21:00:00", "2");
//        Optional<ParkingInfo> p = parkingInfoDao.findById(Long.valueOf(msg.getMsg()));
//        String clientReceiveTopic = p.get().getClientReceiveTopic();
//        mqttMsgSendHandler.isEmpty(clientReceiveTopic, JSON.toJSONString(msg));
        mqttMsgSendHandler.close(String.valueOf(2));
    }

}

