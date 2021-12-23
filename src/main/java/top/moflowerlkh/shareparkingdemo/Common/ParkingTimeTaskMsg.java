package top.moflowerlkh.shareparkingdemo.Common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParkingTimeTaskMsg extends TimeTaskMsg {

    public ParkingTimeTaskMsg(String beginTime, String endTime, String msg) {
        super(beginTime, endTime, msg);
    }

}
