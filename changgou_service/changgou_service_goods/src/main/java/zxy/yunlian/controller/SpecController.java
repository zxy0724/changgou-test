package zxy.yunlian.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import zxy.yunlian.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spec")
public class SpecController {
    @Autowired
    private SpecService specService;
    @GetMapping("/category/{categoryName}")
    public Result<List<Map>> findSpecListByCategoryName(@PathVariable String categoryName){
        List<Map> specList = specService.findSpecListByCategoryName(categoryName);
        return new Result<>(true, StatusCode.OK,"查询成功",specList);
    }

}
