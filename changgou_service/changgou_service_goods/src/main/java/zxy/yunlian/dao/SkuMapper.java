package zxy.yunlian.dao;

import zxy.yunlian.pojo.Sku;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface SkuMapper extends Mapper<Sku> {


    //回滚库存(增加库存并扣减销量)
    @Update("update tb_sku set num=num+#{num},sale_num=sale_num-#{num} where id=#{skuId}")
    void resumeStockNum(@Param("skuId")String skuId,@Param("num")Integer num);
}
