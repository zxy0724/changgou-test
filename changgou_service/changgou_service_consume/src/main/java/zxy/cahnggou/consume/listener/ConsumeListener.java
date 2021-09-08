package zxy.cahnggou.consume.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import zxy.cahnggou.consume.dao.SeckillOrderMapper;
import zxy.cahnggou.consume.service.SecKillOrderService;
import zxy.changgou.seckill.pojo.SeckillOrder;

import java.io.IOException;


@Controller
public class ConsumeListener {
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private SecKillOrderService secKillOrderService;
    public void receiveSeckillOrderMessage(Channel channel, Message message){
        try {
            channel.basicQos(300);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //转换JSON格式
        SeckillOrder seckillOrder = JSON.parseObject(message.getBody(), SeckillOrder.class);
        //基于业务层同步数据库
        int result = secKillOrderService.createOrder(seckillOrder);
        if (result > 0) {
            //同步成功并返回通知
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            //同步失败并返回通知
            try {
                /**
                 * 第一个参数:消息的唯一标识
                 * 第二个参数: true所有消费者都会拒绝这个消息,false只有当前消费者拒绝
                 * 第三个参数:true当前消息会进入到死信队列(延迟消息队列),false当前的消息会重新进入到原有队列中,默认回到头部
                 */
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
