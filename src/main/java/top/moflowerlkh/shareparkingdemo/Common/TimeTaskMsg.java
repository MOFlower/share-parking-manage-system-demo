package top.moflowerlkh.shareparkingdemo.Common;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class TimeTaskMsg {
    String beginTime;
    String endTime;
    String msg;

    public int getDelayTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Time now = Time.valueOf(simpleDateFormat.format(new Date()));
        return (int) (Time.valueOf(endTime).getTime() - now.getTime());
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

    public static TimeTaskMsg getTimeTaskMsgFromString(String s) {
        Map<String, String> msgMap = JSON.parseObject(s, new TypeReference<>() {
        });
        ParkingTimeTaskMsg p = new ParkingTimeTaskMsg(
                msgMap.get("beginTime"), msgMap.get("endTime"), msgMap.get("msg"));
        return p;
    }
}
