package zxy.changgou.user.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/getAll")
    @ResponseBody
    public Result getAll(){
        List<User> userList = userService.getAll();
        return new Result(true,StatusCode.OK,"查询成功",userList);
    }
    @GetMapping
    public Result getUser(){
        List<User> userList = userService.getAll();
        return new Result(true,StatusCode.OK,"查询成功",userList);
    }

    @GetMapping("/load/{username}")
    public User findUserInfo(@PathVariable("username") String username){
        User user = userService.findUserInfo(username);
        return user;
    }
}
