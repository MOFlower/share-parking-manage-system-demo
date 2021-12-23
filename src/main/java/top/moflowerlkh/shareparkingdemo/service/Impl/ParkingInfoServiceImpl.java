package top.moflowerlkh.shareparkingdemo.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.moflowerlkh.shareparkingdemo.Common.ParkingTimeTaskMsg;
import top.moflowerlkh.shareparkingdemo.Common.TimeInterval;
import top.moflowerlkh.shareparkingdemo.dao.ParkingInfoDao;
import top.moflowerlkh.shareparkingdemo.model.Car;
import top.moflowerlkh.shareparkingdemo.model.ParkingInfo;
import top.moflowerlkh.shareparkingdemo.model.User;
import top.moflowerlkh.shareparkingdemo.service.ParkingInfoService;
import top.moflowerlkh.shareparkingdemo.service.ParkingTimePlanService;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:parkingInfo.properties")
@Slf4j
public class ParkingInfoServiceImpl implements ParkingInfoService {
    @Value("${parking.register.identity.code}")
    private static String IDENTITY_CODE;

    @Autowired
    ParkingInfoDao parkingInfoDao;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserServiceImpl userService;

    @Override
    public Map<ParkingInfo, List<TimeInterval>> getAllParkingInfo() {
        Map<ParkingInfo, List<TimeInterval>> result = new HashMap<>();
        for (ParkingInfo p : parkingInfoDao.findAll()) {
            List<TimeInterval> timeIntervals = getFreeTimeOfParking(p.getId());
            result.put(p, timeIntervals);
        }
        return result;
    }

    @Override
    public Map<ParkingInfo, List<TimeInterval>> getParkInfoOfUser(User u) {
        Map<ParkingInfo, List<TimeInterval>> result = new HashMap<>();
        for (ParkingInfo p : u.getOwningParking()) {
            List<TimeInterval> timeIntervals = getFreeTimeOfParking(p.getId());
            result.put(p, timeIntervals);
        }
        return result;
    }

    @Override
    public List<TimeInterval> getFreeTimeOfParking(long parkingID) {
        List<String> freeTime = stringRedisTemplate.opsForList().range(RedisPreKey.freeTimeRedis(parkingID), 0, -1);
        return freeTime == null ? null : freeTime.stream().map(TimeInterval::toTimeInterval).collect(Collectors.toList());
    }

    @Override
    public boolean setFreeTimeOfParking(long parkingID, TimeInterval timeInterval) {
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(RedisPreKey.freeTimeRedis(parkingID)))) {
            stringRedisTemplate.opsForList().rightPush(RedisPreKey.freeTimeRedis(parkingID), timeInterval.toString());
            return true;
        }
        List<TimeInterval> freeTimeInterval = stringRedisTemplate.opsForList()
                .range(RedisPreKey.freeTimeRedis(parkingID), 0, -1)
                .stream().map(TimeInterval::toTimeInterval).collect(Collectors.toList());
        for (int i = 0; i < freeTimeInterval.size(); i++) {
            var it = freeTimeInterval.get(i);
            if (TimeInterval.stringTimeComparator(it.getBeginTime(), timeInterval.getBeginTime()) > 0) {
                freeTimeInterval.add(i, timeInterval);
                for (int j = i - 1 >= 0 ? i - 1 : i; j < freeTimeInterval.size() - 1; ) {
                    if (freeTimeInterval.get(j).addAnotherTimeInterval(freeTimeInterval.get(j + 1))) {
                        freeTimeInterval.remove(j + 1);
                    } else {
                        j++;
                    }
                }
            }
        }
        List<String> freeTimeIntervalString = freeTimeInterval.stream().map(TimeInterval::toString).collect(Collectors.toList());
        stringRedisTemplate.delete(RedisPreKey.freeTimeRedis(parkingID));
        stringRedisTemplate.opsForList().rightPushAll(
                RedisPreKey.freeTimeRedis(parkingID), freeTimeIntervalString);
        return true;
    }

    @Override
    public boolean setUsingTimeOfParking(long parkingID, TimeInterval timeInterval) {
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(RedisPreKey.freeTimeRedis(parkingID)))) {
            return false;
        }
        List<TimeInterval> freeTimeInterval = stringRedisTemplate.opsForList()
                .range(RedisPreKey.freeTimeRedis(parkingID), 0, -1)
                .stream().map(TimeInterval::toTimeInterval).collect(Collectors.toList());
        for (int i = 0; i < freeTimeInterval.size(); i++) {
            if (freeTimeInterval.get(i).removeIsAvailable(timeInterval)) {
                List<TimeInterval> ans = null;
                try {
                    ans = freeTimeInterval.get(i).removeAnotherTimeInterval(timeInterval);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                freeTimeInterval.remove(i);
                freeTimeInterval.add(i, ans.get(1));
                freeTimeInterval.add(i, ans.get(0));
                break;
            }
        }
        List<String> freeTimeIntervalString = freeTimeInterval
                .stream().map(TimeInterval::toString).collect(Collectors.toList());
        stringRedisTemplate.delete(RedisPreKey.freeTimeRedis(parkingID));
        stringRedisTemplate.opsForList().rightPushAll(
                RedisPreKey.freeTimeRedis(parkingID), freeTimeIntervalString);

        return true;
    }

    @Override
    public boolean carIsAvailable(String parkingID, String carNumber) {
        Optional<ParkingInfo> parkingInfo = parkingInfoDao.findById(Long.valueOf(parkingID));
        if (parkingInfo.isEmpty()) {
            return false;
        }
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(RedisPreKey.freeTimeRedis(parkingID)))) {
            for (var timeInterval : stringRedisTemplate.opsForList().range(
                            RedisPreKey.freeTimeRedis(parkingID), 0, -1)
                    .stream().map(TimeInterval::toTimeInterval).collect(Collectors.toList())) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                String nowTimeString = simpleDateFormat.format(new Date());
                if (timeInterval.removeIsAvailable(new TimeInterval(nowTimeString, nowTimeString))) {
                    return false;
                }
            }
        }
        List<Car> owningCar = parkingInfo.get().getOwner().getOwningCar();
        boolean carIsOk = owningCar.stream().map(Car::getCarNumber).collect(Collectors.toList())
                .contains(carNumber);
        if (carIsOk) {
            return true;
        }
        return false;
    }

    @Override
    public Map<String, String> registerParkingInfo(String uniqueCode,
                                                   String identityCode,
                                                   String parkingName,
                                                   double longitude,
                                                   double latitude,
                                                   User owner) throws Exception {
        if (!identityCode.equals(IDENTITY_CODE)) {
            throw new Exception("identity code is false");
        }
        Map<String, String> ret = new HashMap<>();
        ParkingInfo parkingInfo = new ParkingInfo();
        parkingInfo.setParkingName(parkingName);
        parkingInfo.setOwner(owner);
        parkingInfo.setLongitude(longitude);
        parkingInfo.setLatitude(latitude);
        parkingInfo.setUniqueCode(uniqueCode);
        parkingInfo.setClientReceiveTopic(
                MD5Encoder.encode((uniqueCode + "receive-topic").getBytes(StandardCharsets.UTF_8)));
        parkingInfo.setClientSendTopic(
                MD5Encoder.encode((uniqueCode + "send-topic").getBytes(StandardCharsets.UTF_8)));
        parkingInfoDao.save(parkingInfo);

        userService.addParkingInfo(owner.getUsername(), parkingName);
        ret.put("receive-topic", parkingInfo.getClientReceiveTopic());
        ret.put("send-topic", parkingInfo.getClientSendTopic());
        return ret;
    }
}

class RedisPreKey {
    final static String separator = ":";

    private final static String freeTime = "FREETIME" + separator;

    static String freeTimeRedis(String s) {
        return freeTime + s;
    }

    static String freeTimeRedis(long l) {
        return freeTimeRedis(String.valueOf(l));
    }
}
