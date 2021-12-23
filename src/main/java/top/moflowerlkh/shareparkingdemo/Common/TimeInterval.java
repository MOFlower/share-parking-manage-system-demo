package top.moflowerlkh.shareparkingdemo.Common;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class TimeInterval {
    String beginTime;
    String endTime;

    public boolean addAnotherTimeInterval(TimeInterval t) {
        if (stringTimeComparator(endTime, t.beginTime) < 0) {
            return false;
        }
        beginTime = stringTimeComparator(beginTime, t.beginTime) > 0
                ? t.beginTime
                : beginTime;
        endTime = stringTimeComparator(endTime, t.endTime) < 0
                ? t.endTime
                : endTime;
        return true;
    }

    public boolean removeIsAvailable(TimeInterval t) {
        return stringTimeComparator(beginTime, t.beginTime) <= 0
                && stringTimeComparator(endTime, t.endTime) >= 0;
    }

    public List<TimeInterval> removeAnotherTimeInterval(TimeInterval t) throws Exception {
        if (!removeIsAvailable(t)) {
            throw new Exception("remove is no available!");
        }
        List<TimeInterval> result = new ArrayList<>(2);
        result.add(0, new TimeInterval(beginTime, t.beginTime));
        result.add(1, new TimeInterval(t.endTime, endTime));
        return result;
    }

    public static int stringTimeComparator(String lhs, String rhs) {
        return Time.valueOf(lhs).compareTo(Time.valueOf(rhs));
    }


    public String toString() {
        return beginTime + "->" + endTime;
    }

    public static TimeInterval toTimeInterval(String s) {
        return new TimeInterval(s.split("->")[0], s.split("->")[1]);
    }

}
