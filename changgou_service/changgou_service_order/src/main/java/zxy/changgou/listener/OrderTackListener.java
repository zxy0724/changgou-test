package zxy.changgou.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxy.changgou.config.RabbitMQConfig;
import zxy.changgou.service.OrderService;

@Component
public class OrderTackListener {
    @Autowired
    private OrderService orderService;
    @RabbitListener(queues = RabbitMQConfig.ORDER_TACK)
    public void autoTack(String message){
        System.out.println("收到自动确认收货消息");
        orderService.autoTack();//自动确认收货
    }
}

