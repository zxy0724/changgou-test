package zxy.changgou.service;

import com.changgou.entity.Result;
import zxy.changgou.util.AuthToken;

public interface AuthService {
    AuthToken login(String username, String password, String clientId, String clientSecret);

    /**
     * 查询所有用户
     * @return
     */
    public Result getAll();
}
