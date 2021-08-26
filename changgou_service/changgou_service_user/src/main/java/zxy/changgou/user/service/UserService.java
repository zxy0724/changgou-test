package zxy.changgou.user.service;

import com.changgou.entity.Result;
import zxy.changgou.user.pojo.User;

import java.util.List;

public interface UserService {
    /**
     * 查询所有的用户
     * @return
     */
    public List<User> getAll();

    /**
     * 根据username查询当前用户信息
     * @param username
     * @return
     */
    User findUserInfo(String username);

    /**
     * 根据删除用户
     * @param id
     */
    void delete (String id);
}
