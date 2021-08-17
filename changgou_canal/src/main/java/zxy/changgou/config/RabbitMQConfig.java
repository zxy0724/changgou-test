package zxy.changgou.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //交换机名称
    public static final String GOODS_UP_EXCHANGE = "goods_up_exchange";

    //定义队列名称
    public static final String SEARCH_ADD_QUEUE = "search_add_queue";

    //定义队列名称
    public static final String AD_UPDATE_QUEUE = "ad_update_queue";

    //声明队列
    @Bean
    public Queue queue() {
        return new Queue(AD_UPDATE_QUEUE);
    }

    //声明队列
    @Bean(SEARCH_ADD_QUEUE)
    public Queue SEARCH_ADD_QUEUE() {
        return new Queue(SEARCH_ADD_QUEUE);
    }

    //声明交换机
    //durable  持久化操作
    @Bean(GOODS_UP_EXCHANGE)
    public Exchange GOODS_UP_EXCHANGE() {
        return ExchangeBuilder.fanoutExchange(GOODS_UP_EXCHANGE).durable(true).build();
    }

    //队列绑定交换机
    @Bean
    public Binding GOODS_UP_EXCHANGE_BINDING(@Qualifier(SEARCH_ADD_QUEUE) Queue queue, @Qualifier(GOODS_UP_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }
}
