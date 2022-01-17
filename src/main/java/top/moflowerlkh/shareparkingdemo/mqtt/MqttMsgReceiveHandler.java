package top.moflowerlkh.shareparkingdemo.mqtt;

import static top.moflowerlkh.shareparkingdemo.mqtt.MqttConfig.CHANNEL_NAME_IN;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import lombok.extern.slf4j.Slf4j;
import top.moflowerlkh.shareparkingdemo.Common.ParkingTimeTaskMsg;
import top.moflowerlkh.shareparkingdemo.Common.TimeTaskMsg;
import top.moflowerlkh.shareparkingdemo.dao.ParkingInfoDao;
import top.moflowerlkh.shareparkingdemo.model.ParkingInfo;
import top.moflowerlkh.shareparkingdemo.service.ParkingInfoService;
import top.moflowerlkh.shareparkingdemo.service.ParkingTimePlanService;

@Configuration
@Slf4j
public class MqttMsgReceiveHandler {
    @Autowired
    MqttMsgSendHandler mqttMsgSendHandler;
    @Autowired
    ParkingInfoDao parkingInfoDao;
    @Autowired
    ParkingInfoService parkingInfoService;
    @Autowired
    ParkingTimePlanService parkingTimePlanService;

    /**
     * MQTT消费端消息处理器（消息入站
     *
     * @return {@link MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_IN)
    public MessageHandler inboundHandler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                // log info
                Map<String, Object> headers = message.getHeaders();
                for (Map.Entry<String, Object> entry : headers.entrySet()) {
                    log.info("----MQTT Headers---- key = {}, value = {}",
                            entry.getKey(), entry.getValue());
                }
                log.info((String) message.getPayload());

                // logic resolve
                String topic = (String) headers.get("mqtt_receivedTopic");
                ParkingInfo p = parkingInfoDao.findByClientSendTopic(topic);
                // get function
                Map<String, String> deviceFunction = JSON
                        .parseObject(String.valueOf(message.getPayload()), new TypeReference<>() {
                        });
                // get args
                Map<String, String> argsMap = JSON.parseObject(deviceFunction.get("args"), Map.class);

                switch (deviceFunction.get("method")) {
                    // isAvailable function
                    case "isAvailable":
                        if (parkingInfoService.carIsAvailable(
                                String.valueOf(p.getId()),
                                argsMap.get("number"))) {
                            mqttMsgSendHandler.open(p.getClientReceiveTopic());
                        } else {
                            // todo send failed msg to frontend.
                        }
                        break;
                    // isEmptyResponse function
                    case "isEmptyResponse":
                        Map<String, String> msgMap = JSON.parseObject(argsMap.get("msg"), new TypeReference<>() {
                        });
                        TimeTaskMsg msg = new ParkingTimeTaskMsg(
                                msgMap.get("beginTime"),
                                msgMap.get("endTime"),
                                msgMap.get("msg"));
                        if (argsMap.get("isEmpty").equals("true")) {
                            mqttMsgSendHandler.close(p.getClientReceiveTopic());
                            parkingTimePlanService.cancelTimePlanTaskFinally(msg);
                        } else {
                            parkingTimePlanService.continueTimePlanTask(msg);
                        }
                        break;
                    // no defined method
                    default:
                        try {
                            throw new Exception("method is not defined!" + deviceFunction.get("method"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        };
    }
}
