package zxy.changgou.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxy.changgou.config.RabbitMQConfig;
import zxy.changgou.service.PageService;

@Component
public class PageListener {
    @Autowired
    private PageService pageService;

    @RabbitListener(queues = RabbitMQConfig.PAGE_CREATE_QUEUE)
    public void receiveMessage(String spuId){
        System.out.println("生成商品详情页,商品id为："+spuId);
        //生成静态页面
        pageService.generateItemPage(spuId);
    }
}
