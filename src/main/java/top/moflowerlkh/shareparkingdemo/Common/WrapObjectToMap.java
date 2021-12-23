package top.moflowerlkh.shareparkingdemo.Common;

import java.util.HashMap;
import java.util.Map;

public class WrapObjectToMap {
    public static <K, V> Map<String, Object> setMapWithID(Map<K, V> map) {
        Map<String, Object> result = new HashMap<>();
        int cnt = 0;
        for (var m : map.keySet()) {
            HashMap<String, Object> buf = new HashMap<>();
            buf.put("key", m);
            buf.put("value", map.get(m));
            result.put(String.valueOf(cnt++), buf);
        }
        return result;
    }
}
