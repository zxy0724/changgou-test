package zxy.changgou.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxy.changgou.service.OrderService;

@Component
public class OrderTimeOutListener {
    @Autowired
    private OrderService orderService;

    /**
     * 更新支付状态
     * @param message
     */
    @RabbitListener(queues = "queue.ordertimeout")
    public void receiveCloseOrderMessage(String message){
        System.out.println("接到关闭订单的消息："+message);
        try {
            orderService.closeOrder(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
