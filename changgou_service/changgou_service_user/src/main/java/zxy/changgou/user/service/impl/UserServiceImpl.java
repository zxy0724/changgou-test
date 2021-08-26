package zxy.changgou.user.service.impl;

import com.changgou.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zxy.changgou.user.dao.UserMapper;
import zxy.changgou.user.pojo.User;
import zxy.changgou.user.service.UserService;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    /**
     * 查询所有的用户
     *
     * @return
     */
    @Override
    public List<User> getAll() {
        return userMapper.selectAll();
    }

    /**
     * 根据username查询当前用户信息
     *
     * @param username
     * @return
     */
    @Override
    public User findUserInfo(String username) {
        return userMapper.selectByPrimaryKey(username);
    }

    /**
     * 根据删除用户
     * @param id
     */
    @Override
    public void delete(String id) {
        userMapper.selectByPrimaryKey(id);
    }
}
