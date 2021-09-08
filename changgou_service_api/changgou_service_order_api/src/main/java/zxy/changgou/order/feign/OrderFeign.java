package zxy.changgou.order.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import zxy.changgou.pojo.Order;

@FeignClient(name="order")
public interface OrderFeign {
    /**
     * 提交订单数据
     * @param order
     * @return
     */
    @PostMapping("/order")
    public Result add(@RequestBody Order order);

    /**
     * 根据id查询数据
     * @param orderId
     * @return
     */
    @GetMapping("/order/{id}")
    public Result<Order> findById(@PathVariable("id") String orderId);

}
