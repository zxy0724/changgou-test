package zxy.changgou.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public final static  String ORDER_TACK="order_tack";
    @Bean
    public Queue queue(){
        return new Queue(ORDER_TACK);
    }
}
