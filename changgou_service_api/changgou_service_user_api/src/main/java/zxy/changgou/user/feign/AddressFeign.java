package zxy.changgou.user.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import zxy.changgou.user.pojo.Address;

import java.util.List;

@FeignClient(name="user")
public interface AddressFeign {
    /***
     * 查询用户的收件地址信息
     * @return
     */
    @GetMapping(value = "/address/list")
    Result<List<Address>> list();
}