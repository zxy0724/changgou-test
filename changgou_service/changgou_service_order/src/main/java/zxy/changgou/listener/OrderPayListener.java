package zxy.changgou.listener;

import com.alibaba.fastjson.JSON;
import lombok.experimental.Accessors;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxy.changgou.config.RabbitMQConfig;
import zxy.changgou.service.OrderService;

import java.util.Map;

@Component
public class OrderPayListener {
    @Autowired
    private OrderService orderService;
    @RabbitListener(queues = RabbitMQConfig.ORDER_PAY)
    public void receivePayMessage(String message){
        System.out.println("接收到了订单支付的消息："+message);
        Map map = JSON.parseObject(message, Map.class);
        //调用业务层
        orderService.updatePayStatus((String) map.get("orderId"), (String) map.get("transactionId"));
    }
}
