package zxy.changgou.listener;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxy.changgou.config.RabbitMQConfig;
import zxy.changgou.pojo.Task;
import zxy.changgou.service.TaskService;

@Component
public class DelTaskListener {
    @Autowired
    private TaskService taskService;

    @RabbitListener(queues = RabbitMQConfig.CG_BUYING_FINISHADDPOINT)
    public void receiveDelTaskMessage(String message) {
        System.out.println("订单服务接收到了删除任务操作的消息");
        Task task = JSON.parseObject(message, Task.class);
        //删除原有的任务数据,并向历史任务表中添加记录
        taskService.delTask(task);
    }
}
