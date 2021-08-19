package zxy.yunlian.controller;

import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;

import zxy.yunlian.pojo.Brand;
import zxy.yunlian.service.BrandService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin //处理跨域
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping
    public Result<List<Brand>> findAll(){
        List<Brand> brandList = brandService.findAll();
        return new Result<List<Brand>>(true, StatusCode.OK,"查询成功",brandList);
    }
    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result<Brand> findById(@PathVariable(value = "id") Integer id){
        Brand brand = brandService.findById(id);
        /*if(null == brand){
            throw new RuntimeException("id="+id+"所对应的记录不存在");
        }*/
        return new Result<Brand>(true,StatusCode.OK,"查询成功",brand);
    }
    /***
     * 新增品牌数据
     * @param brand
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Brand brand){
        brandService.add(brand);
        return new Result(true,StatusCode.OK,"添加成功");
    }
    /**
     * 修改
     * @param brand
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Brand brand,@PathVariable("id") Integer id){
        brand.setId(id);
        brandService.update(brand);
        return new Result(true,StatusCode.OK,"修改成功");
    }
    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable(value = "id") Integer id){
        brandService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }
    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search")
    public Result findList(@RequestParam Map searchMap){
        List<Brand> list = brandService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }
    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result findPage(@PathVariable(name = "page") int page,@PathVariable(value = "size") int size){
        Page<Brand> pageInfo = brandService.findPage(page,size);
        PageResult pageResult = new PageResult(pageInfo.getTotal(),pageInfo.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }
    /***
     * 分页+条件搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/searchPage/{page}/{size}")
    public Result findPage(@RequestParam Map searchMap,@PathVariable(value = "page") int page,@PathVariable("size") int size){
        //int i =2/0;
        Page<Brand> pageList = brandService.findPage(searchMap, page, size);
        PageResult pageResult = new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }
    /**
     * 根据分类名称来查询品牌的名称和图片
     * @param categoryName
     * @return
     */
    @GetMapping(value = "/category/{categoryName}")
    public Result<List<Map>> findBrandListByCategoryName(@PathVariable("categoryName") String categoryName) {
        List<Map> brandList = brandService.findBrandListByCategoryName(categoryName);
        return new Result<List<Map>>(true,StatusCode.OK,"查询成功",brandList);
    }
}
