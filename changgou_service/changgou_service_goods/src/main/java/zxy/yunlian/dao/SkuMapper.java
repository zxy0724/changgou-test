package zxy.yunlian.dao;

import zxy.changgou.pojo.OrderItem;
import zxy.yunlian.pojo.Sku;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface SkuMapper extends Mapper<Sku> {


    //回滚库存(增加库存并扣减销量)
    @Update("update tb_sku set num=num+#{num},sale_num=sale_num-#{num} where id=#{skuId}")
    void resumeStockNum(@Param("skuId") String skuId, @Param("num") Integer num);

    /**
     * 递减库存
     *
     * @param orderItem
     * @return
     */
    @Update("UPDATE tb_sku SET num=num-#{num},sale_num=sale_num+#{num} WHERE id=#{skuId} AND num>=#{num}")
    int decrCount(OrderItem orderItem);
}
