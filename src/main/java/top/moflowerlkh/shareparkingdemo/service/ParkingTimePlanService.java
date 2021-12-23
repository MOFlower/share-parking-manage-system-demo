package top.moflowerlkh.shareparkingdemo.service;


import top.moflowerlkh.shareparkingdemo.Common.TimeTaskMsg;

public interface ParkingTimePlanService {
    boolean submitTimePlanTask(TimeTaskMsg msg);

    boolean tryCancelTimePlanTask(TimeTaskMsg msg);

    boolean cancelTimePlanTaskFinally(TimeTaskMsg msg);

    boolean continueTimePlanTask(TimeTaskMsg msg);
}
