package zxy.changgou.user.dao;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import zxy.changgou.user.pojo.User;
@Repository
public interface UserMapper extends Mapper<User> {
}
