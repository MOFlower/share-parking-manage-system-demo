package top.moflowerlkh.shareparkingdemo.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.moflowerlkh.shareparkingdemo.Common.ParkingTimeTaskMsg;
import top.moflowerlkh.shareparkingdemo.Common.TimeInterval;
import top.moflowerlkh.shareparkingdemo.Common.WrapObjectToMap;
import top.moflowerlkh.shareparkingdemo.model.User;
import top.moflowerlkh.shareparkingdemo.service.ParkingInfoService;
import top.moflowerlkh.shareparkingdemo.service.ParkingTimePlanService;
import top.moflowerlkh.shareparkingdemo.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Api(tags = "停车位模块")
@RestController
public class ParkInfoController {
    @Autowired
    ParkingInfoService parkingInfoService;
    @Autowired
    ParkingTimePlanService parkingTimePlanService;
    @Autowired
    UserService userService;

    @ApiOperation("查询所有停车位信息")
    @GetMapping("/parking/info")
    public String getAllParkingInfo() {
        return JSON.toJSONString(
                WrapObjectToMap.setMapWithID(parkingInfoService.getAllParkingInfo()));
    }

    @ApiOperation("查询自己的停车位管理信息")
    @GetMapping("parking/info/me")
    public String getParkingInfo(@ApiIgnore HttpSession httpSession) {
        String username = (String) httpSession.getAttribute("username");
        if (username == null) {
            return "not login";
        }
        User u = userService.getUserByName(username);
        if (u == null) {
            return "not login";
        }
        return JSON.toJSONString(
                WrapObjectToMap.setMapWithID(parkingInfoService.getParkInfoOfUser(u)));
    }


    @ApiOperation("设置可以占用的时间")
    @PostMapping("/parking/{parking-id}/free-time")
    public String setFreeTime(
            @ApiParam(name = "beginTime", value = "可以占用的开始时间(example = '20:00:01')",
                    example = "20:00:01")
            @RequestParam("beginTime")
                    String beginTime,
            @ApiParam(name = "endTime", value = "结束占用的时间", example = "20:30:01")
            @RequestParam("endTime")
                    String endTime,
            @ApiParam(name = "parking-id", value = "选取得停车位 ID", example = "1201320")
            @PathVariable("parking-id")
                    String parkingID) {
        TimeInterval timeInterval = new TimeInterval(beginTime, endTime);
        // 更新空闲时间
        parkingInfoService.setFreeTimeOfParking(
                Long.parseLong(parkingID), timeInterval);
        return "success";
    }

    @ApiOperation("选择车位租赁指定时间")
    @PostMapping("/parking/{parking-id}/using-time")
    public String setUsingTime(
            @ApiParam(name = "beginTime", value = "开始租赁的时间", example = "2020-10-20 20:00:01")
            @RequestParam("beginTime")
                    String beginTime,
            @ApiParam(name = "endTime", value = "结束租赁的时间", example = "2020-10-20 20:30:01")
            @RequestParam("endTime")
                    String endTime,
            @ApiParam(name = "parking-id", value = "选取得停车位 ID", example = "1201320")
            @PathVariable("parking-id") String parkingID) {
        TimeInterval timeInterval = new TimeInterval(beginTime, endTime);
        if (parkingInfoService.setUsingTimeOfParking(
                Long.parseLong(parkingID), new TimeInterval(beginTime, endTime))) {
            // 设置定时任务
            ParkingTimeTaskMsg parkingTimeTaskMsg = new ParkingTimeTaskMsg(
                    timeInterval.getBeginTime(),
                    timeInterval.getEndTime(),
                    parkingID
            );
            parkingTimePlanService.submitTimePlanTask(parkingTimeTaskMsg);
            return "success";
        }
        return "failed";
    }

    //  @ApiIgnore
    @ApiOperation("车辆是否具有停车权限")
    @GetMapping("/parking/{parking-id}/want-parking")
    public String carIsAvailable(@PathVariable("parking-id") String parkingID,
                                 @RequestParam("carNumber") String carNumber) {
        if (parkingInfoService.carIsAvailable(parkingID, carNumber)) {
            return "success";
        }
        return "failed";
    }

    @ApiOperation("车位注册")
    @GetMapping("/parking/register/{unique-id}")
    public String registerParkingInfo(@PathVariable("unique-id") String uniqueCode,
                                      @RequestParam("identityCode") String identityCode,
                                      @RequestParam("parkingName") String parkingName,
                                      @RequestParam("longitude") String longitude,
                                      @RequestParam("latitude") String latitude,
                                      @ApiIgnore HttpSession httpSession
    ) {
        String username = (String) httpSession.getAttribute("username");
        User u = userService.getUserByName(username);
        Map<String, String> ret;
        try {
            ret = parkingInfoService.registerParkingInfo(
                    uniqueCode, identityCode, parkingName, Double.valueOf(longitude), Double.valueOf(latitude), u);
        } catch (Exception e) {
            e.printStackTrace();
            return "register failed!!!";
        }
        return JSON.toJSONString(ret);
    }
}
