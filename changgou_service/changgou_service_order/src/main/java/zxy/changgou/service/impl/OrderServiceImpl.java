package zxy.changgou.service.impl;

import com.alibaba.fastjson.JSON;
//import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import com.changgou.util.IdWorker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import zxy.changgou.config.RabbitMQConfig;
import zxy.changgou.dao.*;
import zxy.changgou.pay.feign.WxPayFeign;
import zxy.changgou.pojo.*;
import zxy.changgou.service.CartService;
import zxy.changgou.service.OrderService;
import zxy.yunlian.feign.SkuFeign;

import java.time.LocalDate;
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

    @Autowired
    private WxPayFeign wxPayFeign;

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
//    @GlobalTransactional(name = "order_add")
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
        String orderId = idWorker.nextId() + "";
        order.setId(orderId);
        orderMapper.insertSelective(order);

        //添加订单明细
        for (OrderItem orderItem : orderItemList) {
            orderItem.setId(idWorker.nextId() + "");
            orderItem.setIsReturn("0"); //0:未退货 1:已退货
            orderItem.setOrderId(orderId);
            orderItemMapper.insertSelective(orderItem);
        }
        //库存减库存
        skuFeign.decrCount(order.getUsername());
        //让事务回滚
//        int i = 2 / 0;
        //增加任务表记录
        Task task = new Task();
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setMqExchange(RabbitMQConfig.EX_BUYING_ADDPOINTUSER);
        task.setMqRoutingkey(RabbitMQConfig.CG_BUYING_ADDPOINT_KEY);
        Map map = new HashMap();
        map.put("username", order.getUsername());
        map.put("orderId", order.getId());
        map.put("point", order.getPayMoney());
        task.setRequestBody(JSON.toJSONString(map));
        taskMapper.insertSelective(task);
        //删除购物车数据
        redisTemplate.delete("Cart_" + order.getUsername());
        //向订单生成队列中发送订单编号,同时该队列还设置了相应的过期时间10s,
        // 如果超时，会自动触发消息的转发，发送到Dead Letter Exchange中去
        //在queue.ordertimeout队列上也设置了过期时间，如果超时将被丢弃
        rabbitTemplate.convertAndSend("", "queue.ordercreate", orderId);
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

    /**
     * 修改订单状态为已支付
     *
     * @param orderId
     * @param transactionId
     */
    @Autowired
    private OrderLogMapper orderLogMapper;

    @Override
    public void updatePayStatus(String orderId, String transactionId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        //存在订单且状态为0
        if (order != null && "0".equals(order.getPayStatus())) {
            order.setPayStatus("1");
            order.setOrderStatus("1");
            order.setUpdateTime(new Date());
            order.setPayTime(new Date());
            order.setTransactionId(transactionId);//微信返回的交易流水号
            orderMapper.updateByPrimaryKeySelective(order);
            //记录订单变动日志
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId() + "");
            orderLog.setOperater("system");//系统
            orderLog.setOperateTime(new Date());//当前日期
            order.setOrderStatus("1");
            orderLog.setPayStatus("1");
            orderLog.setRemarks("支付流水号" + transactionId);
            orderLog.setOrderId(order.getId());
            orderLogMapper.insert(orderLog);
        }
    }

    /**
     * 关闭订单
     *
     * @param orderId
     */
    @Transactional
    @Override
    public void closeOrder(String orderId) {
        /**
         * 1.根据订单id查询mysql中的订单信息,判断订单是否存在,判断订单的支付状态
         * 2. 基于微信查询订单信息(微信)
         * 2.1)如果当前订单的支付状态为已支付,则进行数据补偿(mysql)
         * 2.2)如果当前订单的支付状态为未支付,则修改mysql中的订单信息,新增订单日志,恢复商品的库存,基于微信关闭订单
         */
        System.out.println("关闭订单开启：" + orderId);
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在！");
        }
        if (!"0".equals(order.getOrderStatus())) {
            System.out.println("此订单不用关闭");
            return;
        }
        System.out.println("关闭订单校验通过：" + orderId);
        //基于微信查询订单
        Map wxQueryMap = (Map) wxPayFeign.queryOrder(orderId).getData();
        System.out.println("查询微信支付订单：" + wxQueryMap);
        //如已支付则进行数据补偿
        if ("SUCCESS".equals(wxQueryMap.get("trade_state"))) {
            this.updatePayStatus(orderId, (String) wxQueryMap.get("transaction_id"));
            System.out.println("完成数据补偿");
        }
        //如果未支付则修改sql订单信息，新增日志，灰度库存，基于微信关闭订单
        if ("NOTPAY".equals(wxQueryMap.get("trade_state"))) {
            System.out.println("执行关闭");
            order.setUpdateTime(new Date());
            order.setOrderStatus("4");
            orderMapper.updateByPrimaryKeySelective(order);
            //新增日志
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId() + "");
            orderLog.setOperater("system");
            orderLog.setOperateTime(new Date());
            orderLog.setOrderStatus("4");
            orderLog.setOrderId(order.getId());
            orderLogMapper.insert(orderLog);
            //恢复库存
            OrderItem _orderItem = new OrderItem();
            _orderItem.setOrderId(orderId);
            List<OrderItem> orderItemList = orderItemMapper.select(_orderItem);
            for (OrderItem orderItem : orderItemList) {
                skuFeign.resumeStockNum(orderItem.getSkuId(), orderItem.getNum());
            }
            //基于微信关闭订单
            wxPayFeign.closeOrder(orderId);
        }
    }

    /**
     * 批量发货
     *
     * @param orders
     */
    @Override
    @Transactional
    public void batchSend(List<Order> orders) {
        //判断每一个订单的运单号和物流公司的值是否存在
        for (Order order : orders) {
            if (order.getId() == null) {
                throw new RuntimeException("订单号不存在!");
            }
            if (order.getShippingCode() == null || order.getShippingName() == null) {
                throw new RuntimeException("请输入运单号或物流公司的名称");
            }
        }
        //进行订单状态的校验
        for (Order order : orders) {
            Order order1 = orderMapper.selectByPrimaryKey(order.getId());
            if (!"0".equals(order1.getConsignStatus()) || !"1".equals(order1.getOrderStatus())) {
                throw new RuntimeException("订单状态不合法");
            }
        }
        //修改订单的状态为已发货
        for (Order order : orders) {
            order.setOrderStatus("2"); //已发货
            order.setConsignStatus("1");//已发货
            order.setConsignTime(new Date());//发货时间
            order.setUpdateTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);

            //记录订单日志
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId() + "");
            orderLog.setOperateTime(new Date());
            orderLog.setOperater("admin");//系统管理员
            orderLog.setOrderStatus("2");//已完成
            orderLog.setConsignStatus("1");//发状态（0未发货 1已发货）
            orderLog.setOrderId(order.getId());
            orderLogMapper.insertSelective(orderLog);
        }
    }

    @Override
    @Transactional
    public void confirmTask(String orderId, String operator) {

        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!"1".equals(order.getConsignStatus())) {
            throw new RuntimeException("订单未发货");
        }

        order.setConsignStatus("2"); //已送达
        order.setOrderStatus("3"); //已完成
        order.setUpdateTime(new Date());
        order.setEndTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);

        //记录订单日志
        OrderLog orderLog = new OrderLog();
        orderLog.setId(idWorker.nextId() + "");
        orderLog.setOperateTime(new Date());
        orderLog.setOperater(operator);
        orderLog.setOrderStatus("3");
        orderLog.setConsignStatus("2");
        orderLog.setOrderId(order.getId());
        orderLogMapper.insertSelective(orderLog);
    }
    @Autowired
    private OrderConfigMapper orderConfigMapper;
    /**
     * 实现思路:
     *  1.从订单配置表中获取订单自动确认期限
     *  2.得到当前日期，向前数（订单自动确认期限）天。作为过期时间节点
     *  3.从订单表中获取过期订单（发货时间小于过期时间，且为未确认收货状态）
     *  4.循环批量处理，执行确认收货
     */
    @Override
    @Transactional
    public void autoTack() {
        //读取订单配置信息
        OrderConfig orderConfig = orderConfigMapper.selectByPrimaryKey(1);
        //获取当前的时间
        LocalDate now = LocalDate.now();
        LocalDate date = now.plusDays(-orderConfig.getTakeTimeout());
        //按条件查询，获取订单列表
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLessThan("consignTime",date);
        criteria.andEqualTo("orderStatus","2");
        List<Order> orderList = orderMapper.selectByExample(example);
        for (Order order : orderList) {
            this.confirmTask(order.getId(),"system");
        }
    }
}
