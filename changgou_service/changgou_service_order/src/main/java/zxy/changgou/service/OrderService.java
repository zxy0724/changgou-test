package zxy.changgou.service;

import zxy.changgou.pojo.Order;

import java.util.List;

public interface OrderService {
    /***
     * 查询所有
     * @return
     */
    List<Order> findAll();

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    Order findById(String id);

    /***
     * 新增
     * @param order
     * @return orderId 返回订单的id
     */
    String add(Order order);

    /***
     * 修改
     * @param order
     */
    void update(Order order);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 修改订单状态为已支付
     * @param orderId
     * @param transactionId
     */
    void updatePayStatus(String orderId, String transactionId);

    /***
     * 关闭订单
     * @param orderId
     */
    void closeOrder(String orderId);

    /**
     * 批量发货
     *
     * @param orders
     */
    void batchSend(List<Order> orders);

    /***
     * 确认收货
     * @param orderId
     * @param operator
     */
    void confirmTask(String orderId, String operator);

    /***
     * 自动确认收货
     */
    void autoTack();
}