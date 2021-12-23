package top.moflowerlkh.shareparkingdemo.mqtt;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MqttMsgSendHandler {
    @Autowired
    IMqttSender iMqttSender;

    public void open(String id) {
        iMqttSender.sendToMqtt(
                id,
                2,
                JSON.toJSONString(Map.of("method", "open"))
        );
    }

    public void close(String id) {
        iMqttSender.sendToMqtt(
                id,
                2,
                JSON.toJSONString(Map.of("method", "close"))
        );
    }

    public void isEmpty(String id, String msg) {
        iMqttSender.sendToMqtt(
                id,
                2,
                JSON.toJSONString(Map.of(
                        "method", "isEmpty",
                        "args", msg))
        );
    }
}
