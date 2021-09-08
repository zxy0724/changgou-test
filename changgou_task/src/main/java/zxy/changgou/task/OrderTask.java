package zxy.changgou.task;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zxy.changgou.config.RabbitMQConfig;

import java.util.Date;

@Component
public class OrderTask {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 订单自动收货
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoTake(){
        System.out.println(new Date());
        rabbitTemplate.convertAndSend("", RabbitMQConfig.ORDER_TACK,"-");
    }
}
