package zxy.changgou.user.listener;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zxy.changgou.pojo.Task;
import zxy.changgou.user.congfig.RabbitMQConfig;
import zxy.changgou.user.service.UserService;

@Component
public class AddPointListener {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @RabbitListener(queues =RabbitMQConfig.CG_BUYING_ADDPOINT)
    public void receiveAddPointMessage(String message){
        Task task = JSON.parseObject(message, Task.class);
        if (task == null|| StringUtils.isEmpty(task.getRequestBody())) {
            return;
        }
        //判断Redis是否存在内容
        Object value = redisTemplate.boundValueOps(task.getId()).get();
        if (value!=null){
            return;
        }
        //更新用户积分
        int result = userService.updateUserPoint(task);
        if (result <= 0) {
            return;
        }
        //返回通知
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_BUYING_ADDPOINTUSER,RabbitMQConfig.CG_BUYING_ADDPOINT_KEY,JSON.toJSONString(task));
    }
}
