package zxy.yunlian.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import zxy.yunlian.pojo.Sku;

import java.util.List;

@FeignClient(name = "goods")
public interface SkuFeign {
    /***
     *
     */
    @GetMapping("/sku/spu/{spuId}")
    public List<Sku> findSkuListBySpuId(@PathVariable("spuId") String spuId);
}