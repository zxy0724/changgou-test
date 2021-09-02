package zxy.changgou.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.util.IdWorker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import zxy.changgou.config.RabbitMQConfig;
import zxy.changgou.dao.OrderItemMapper;
import zxy.changgou.dao.OrderMapper;
import zxy.changgou.dao.TaskMapper;
import zxy.changgou.pojo.Order;
import zxy.changgou.pojo.OrderItem;
import zxy.changgou.pojo.Task;
import zxy.changgou.service.CartService;
import zxy.changgou.service.OrderService;
import zxy.yunlian.feign.SkuFeign;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired

    private IdWorker idWorker;

    @Autowired

    private OrderItemMapper orderItemMapper;

    @Autowired

    private RedisTemplate redisTemplate;

    @Autowired

    private SkuFeign skuFeign;

    @Autowired

    private TaskMapper taskMapper;

    @Autowired

    private RabbitTemplate rabbitTemplate;

    /**
     * 查询全部列表
     *
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public Order findById(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public String add(Order order) {
        //获取所有购物项
        Map cartMap = cartService.list(order.getUsername());
        List<OrderItem> orderItemList = (List<OrderItem>) cartMap.get("orderItemList");
        //填充订单数据并保存
        order.setTotalNum((Integer) cartMap.get("totalNum"));
        order.setTotalMoney((Integer) cartMap.get("totalMoney"));
        order.setPayMoney((Integer) cartMap.get("totalMoney"));
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setBuyerRate("0"); // 0:未评价  1:已评价
        order.setSourceType("1"); //1:WEB
        order.setOrderStatus("0"); //0:未完成 1:已完成 2:已退货
        order.setPayStatus("0"); //0:未支付 1:已支付
        order.setConsignStatus("0"); //0:未发货 1:已发货
        String orderId = idWorker.nextId()+"";
        order.setId(orderId);
        orderMapper.insertSelective(order);

        //添加订单明细
        for (OrderItem orderItem : orderItemList) {
            orderItem.setId(idWorker.nextId()+"");
            orderItem.setIsReturn("0"); //0:未退货 1:已退货
            orderItem.setOrderId(orderId);
            orderItemMapper.insertSelective(orderItem);
        }
        //库存减库存
        skuFeign.decrCount(order.getUsername());
        //清除Redis缓存购物车数据
        //增加任务表记录
        Task task = new Task();
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setMqExchange(RabbitMQConfig.EX_BUYING_ADDPOINTUSER);
        task.setMqRoutingkey(RabbitMQConfig.CG_BUYING_ADDPOINT_KEY);
        Map map = new HashMap();
        map.put("username",order.getUsername());
        map.put("orderId",order.getId());
        map.put("point",order.getPayMoney());
        task.setRequestBody(JSON.toJSONString(map));
        taskMapper.insertSelective(task);
        //删除购物车数据
        redisTemplate.delete("Cart_" + order.getUsername());
        //向订单生成队列中发送订单编号,同时该队列还设置了相应的过期时间10s,
        // 如果超时，会自动触发消息的转发，发送到Dead Letter Exchange中去
        //在queue.ordertimeout队列上也设置了过期时间，如果超时将被丢弃
        rabbitTemplate.convertAndSend("","queue.ordercreate",orderId);
        return orderId;
    }

    /**
     * 修改
     *
     * @param order
     */
    @Override
    public void update(Order order) {
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        orderMapper.deleteByPrimaryKey(id);
    }

}
