package zxy.changgou.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import zxy.changgou.config.RabbitMQConfig;

import java.util.HashMap;
import java.util.Map;

@CanalEventListener
public class SpuListener {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @ListenPoint(schema = "changgou_goods",table = "tb_spu")
    public void goodsUp(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        //获取改变之前的数据并将这部分数据转换为map
        Map<String, String> oldData = new HashMap<>();
        rowData.getBeforeColumnsList().forEach((c)->oldData.put(c.getName(),c.getValue()));
        //获取改变之后数据并将这部分数据转换为map
        Map<String, String> newData = new HashMap<>();
        rowData.getBeforeColumnsList().forEach((c)->newData.put(c.getName(),c.getValue()));

        //获取最新上架的商品0->1
        if ("0".equals(oldData.get("is_marketable"))&&"1".equals(newData.get("is_marketable"))){
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_UP_EXCHANGE,"",newData.get("id"));
        }
    }
}