package zxy.yunlian.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import zxy.yunlian.pojo.Category;


@FeignClient(name = "goods")
public interface CategoryFeign {
    @GetMapping("/category/{id}")
    public Result<Category> findById(@PathVariable("id") Integer id);
}