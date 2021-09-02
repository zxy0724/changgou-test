package zxy.changgou.user.dao;

import zxy.changgou.user.pojo.PointLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface PointLogMapper extends Mapper<PointLog> {
    @Select("select * from tb_point_log where order_id=#{orderId}")
    PointLog findPointLogByOrderId(@Param("orderId")String orderId);
}
