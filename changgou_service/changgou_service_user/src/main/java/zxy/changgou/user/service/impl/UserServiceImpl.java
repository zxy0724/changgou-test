package zxy.changgou.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zxy.changgou.pojo.Task;
import zxy.changgou.user.dao.PointLogMapper;
import zxy.changgou.user.dao.UserMapper;
import zxy.changgou.user.pojo.PointLog;
import zxy.changgou.user.pojo.User;
import zxy.changgou.user.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PointLogMapper pointLogMapper;
    @Autowired
    private RedisTemplate redisTemplate;

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
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        userMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int updateUserPoint(Task task) {
        System.out.println("用户服务现在开始对任务进行处理");
        //1.从task中获取相关数据
        Map map = JSON.parseObject(task.getRequestBody(), Map.class);
        String username = map.get("username").toString();
        String orderId = map.get("orderId").toString();
        int point = (int) map.get("point");
        //2.判断当前的任务是否操作过
        PointLog pointLog = pointLogMapper.findPointLogByOrderId(orderId);
        if (pointLog != null) {
            return 0;
        }
        //3.将任务存入到redis中
        redisTemplate.boundValueOps(task.getId()).set("exist",30, TimeUnit.SECONDS);
        //4.修改用户积分
        int result = userMapper.updateUserPoint(username, point);
        if (result <= 0) {
            return 0;
        }
        //5.记录积分日志信息
        PointLog pointLog1 = new PointLog();
        pointLog1.setUserId(orderId);
        pointLog1.setPoint(point);
        int i = pointLogMapper.insertSelective(pointLog1);
        if (i <= 0) {
            return 0;
        }
        //6.删除redis中的任务信息
        redisTemplate.delete(task.getId());
        System.out.println("用户服务完成了更改用户积分的操作");
        return 1;
    }
}
