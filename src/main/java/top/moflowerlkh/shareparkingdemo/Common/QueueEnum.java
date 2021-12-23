package top.moflowerlkh.shareparkingdemo.Common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueueEnum {
    QUEUE_TTL_TASK("share.parking.direct.ttl",
            "share.parking.delay.ttl",
            "share.parking.delay.ttl");
    private final String exchangeName;
    private final String queueName;
    private final String routeKeyName;
}
