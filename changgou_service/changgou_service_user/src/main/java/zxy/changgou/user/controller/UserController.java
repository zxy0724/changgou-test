package zxy.changgou.user.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zxy.changgou.user.pojo.User;
import zxy.changgou.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 查询当前用户信息
     *
     * @return
     */
    @GetMapping("/getAll")
    @ResponseBody
    public Result getAll() {
        List<User> userList = userService.getAll();
        return new Result(true, StatusCode.OK, "查询成功", userList);
    }

    @GetMapping
    public Result getUser() {
        List<User> userList = userService.getAll();
        return new Result(true, StatusCode.OK, "查询成功", userList);
    }

    @GetMapping("/load/{username}")
    public User findUserInfo(@PathVariable("username") String username) {
        User user = userService.findUserInfo(username);
        return user;
    }

    /**
     * 根据删除用户
     *
     * @param id
     * @return
     */
    //权限控制，只有admin才能使用刚方法
    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        userService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
