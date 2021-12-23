package top.moflowerlkh.shareparkingdemo.rabbitmq;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitMQTest {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Test
    void delayTest() {

    }
}
