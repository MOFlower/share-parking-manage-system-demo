package top.moflowerlkh.shareparkingdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ShareParkingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShareParkingDemoApplication.class, args);
    }

}