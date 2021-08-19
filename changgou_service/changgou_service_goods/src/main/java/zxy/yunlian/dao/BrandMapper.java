package zxy.yunlian.dao;

import zxy.yunlian.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface BrandMapper extends Mapper<Brand> {
    /**
     * 自定义多表查询
     */
    /**
     * 根据分类名称来查询品牌的名称和图片
     * @param categoryName
     * @return
     */
    @Select("SELECT name,image FROM tb_brand WHERE id in(SELECT brand_id FROM tb_category_brand WHERE category_id in(SELECT id FROM tb_category WHERE name=#{categoryName}))")
    public List<Map> findBrandListByCategoryName(@Param("categoryName")String categoryName);

}
