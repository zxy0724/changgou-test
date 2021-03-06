package zxy.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import zxy.changgou.config.TokenDecode;
import zxy.changgou.service.OrderService;
import zxy.changgou.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        List<Order> orderList = orderService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", orderList);
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id) {
        Order order = orderService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", order);
    }

    @Autowired
    private TokenDecode tokenDecode;

    /***
     * 新增Order数据
     * @param order
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Order order) {
        //获取用户名
        Map<String, String> userMap = tokenDecode.getUserInfo();
        String username = userMap.get("username");
        //设置购买用户
        order.setUsername(username);
        String orderId = orderService.add(order);
        return new Result(true, StatusCode.OK, "添加成功", orderId);
    }


    /***
     * 修改数据
     * @param order
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Order order, @PathVariable String id) {
        order.setId(id);
        orderService.update(order);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        orderService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 这里需要对接第三方物流p
     * @param orders
     * @return
     */
    @PostMapping("/batchSend")
    public Result batchSend(@RequestBody List<Order> orders) {
        orderService.batchSend(orders);
        return new Result(true, StatusCode.OK, "发货成功");
    }

    /***
     * 确认收货
     * @param orderId  订单号
     * @param operator 操作者
     * @return
     */
//    @PutMapping("/take/{orderId}/operator/{operator}")
//    public Result confirmTask(@PathVariable String orderId, @PathVariable String operator) {
//        orderService.confirmTask(orderId, operator);
//        return new Result(true, StatusCode.OK, "");
//    }
    //手动确认收货
    public Result confirmTask(@PathVariable String orderId,@PathVariable String operator){
        orderService.confirmTask(orderId,operator);
        return new Result(true,StatusCode.OK,"");
    }
}
