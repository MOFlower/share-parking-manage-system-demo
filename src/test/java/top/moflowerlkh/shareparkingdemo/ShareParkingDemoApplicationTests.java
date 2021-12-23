package top.moflowerlkh.shareparkingdemo;

import org.junit.jupiter.api.Test;

import javax.sound.midi.Soundbank;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

//@SpringBootTest
class ShareParkingDemoApplicationTests {

    @Test
    void contextLoads() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
//        Time now = Time.valueOf(simpleDateFormat.format(new Date()));
//        Date date = new Date(Time.valueOf("20:45:00").getTime());
//        Date date1 = new Date(now.getTime());
//        System.out.println(date);
//        System.out.println(date1);
//        System.out.println(date.getTime() - date1.getTime());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Time now = Time.valueOf(simpleDateFormat.format(new Date()));
        int i = (int) (Time.valueOf("20:45:00").getTime() - now.getTime());
        System.out.println(i);
    }

}
