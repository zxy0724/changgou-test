package zxy.changgou.user.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import zxy.changgou.user.pojo.User;

@FeignClient(name = "user")
public interface UserFeign {
    @GetMapping("/user/load/{username}")
    public User findUserInfo(@PathVariable("username") String username);

    /**
     * 查询所有的用户信息
     *
     * @return
     */
    @GetMapping("/user")
    public Result getAll();
}
