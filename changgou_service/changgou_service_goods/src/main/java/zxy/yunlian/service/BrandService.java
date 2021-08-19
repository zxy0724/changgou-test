package zxy.yunlian.service;

import zxy.yunlian.pojo.Brand;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface BrandService {
    /**
     * 查询所有品牌
     * @return
     */
    public List<Brand> findAll();
    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public Brand findById(Integer id);
    /***
     * 新增品牌
     * @param brand
     */
    public void add(Brand brand);
    /***
     * 修改品牌数据
     * @param brand
     */
    public void update(Brand brand);
    /***
     * 删除品牌
     * @param id
     */
    public void delete(Integer id);
    /***
     * 多条件搜索品牌方法
     * @param searchMap
     * @return
     */
    public List<Brand> findList(Map<String,Object>searchMap);
    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Brand> findPage(int page, int size);
    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Brand> findPage(Map<String,Object>searchMap,int page,int size);
    /**
     * 根据分类名称来查询品牌的名称和图片
     * @param categoryName
     * @return
     */
    public List<Map> findBrandListByCategoryName(String categoryName);
}
