package zxy.yunlian.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import zxy.yunlian.pojo.Sku;

import java.util.List;

@FeignClient(name = "goods")
public interface SkuFeign {
    @GetMapping("/sku/spu/{spuId}")
    public List<Sku> findSkuListBySpuId(@PathVariable("spuId") String spuId);

    /**
     * 根据id查询sku信息
     *
     * @param id
     * @return
     */
    @GetMapping("/sku/{id}")
    public Result<Sku> findById(@PathVariable("id") String id);

    /***
     * 库存递减
     * @param username
     * @return
     */
    @PostMapping(value = "/sku/decr/count")
    public Result decrCount(@RequestParam(value = "username") String username);

    @RequestMapping("/sku/resumeStockNum")
    public Result resumeStockNum(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num);
}