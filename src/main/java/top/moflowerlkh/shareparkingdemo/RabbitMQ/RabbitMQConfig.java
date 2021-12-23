package top.moflowerlkh.shareparkingdemo.RabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.moflowerlkh.shareparkingdemo.Common.QueueEnum;

import java.util.Map;

@Configuration
public class RabbitMQConfig {
    @Bean
    CustomExchange delayMsgExchange() {
        return new CustomExchange(
                QueueEnum.QUEUE_TTL_TASK.getExchangeName(),
                "x-delayed-message",
                true,
                false,
                Map.of("x-delayed-type", "direct")
        );
    }

    @Bean
    Queue delayMsgQueue() {
        return new Queue(QueueEnum.QUEUE_TTL_TASK.getQueueName());
    }

    @Bean
    Binding DelayMsgQueueBinding(Queue delayMsgQueue, CustomExchange delayMsgExchange) {
        return BindingBuilder
                .bind(delayMsgQueue)
                .to(delayMsgExchange)
                .with(QueueEnum.QUEUE_TTL_TASK.getRouteKeyName())
                .noargs();
    }
}
