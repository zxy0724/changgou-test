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


}
